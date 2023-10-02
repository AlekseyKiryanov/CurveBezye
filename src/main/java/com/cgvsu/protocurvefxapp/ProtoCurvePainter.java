package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurvePainter {

    private final GraphicsContext graphicsContext;

    public ProtoCurvePainter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public void paintDot(double a, double b, Color color) {

        int x = (int) a;
        int y = (int) b;

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int dotSize = 3;
        for (int row = y - dotSize / 2 - 1; row < y + dotSize; ++row)
            for (int col = x - dotSize / 2 - 1; col < x + dotSize; ++col)
                pixelWriter.setColor(col, row, color);
    }

    public void paintDot(Point2D p, Color color) {

        int x = (int) p.getX();
        int y = (int) p.getY();

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int dotSize = 3;
        for (int row = y - dotSize / 2 - 1; row < y + dotSize; ++row)
            for (int col = x - dotSize / 2 - 1; col < x + dotSize; ++col)
                pixelWriter.setColor(col, row, color);
    }

    public void clear(){
       graphicsContext.clearRect(0,0,8000,6000);
    }

    public void paintBrokenLine(ArrayList<Point2D> points, Color color){
        if (points.size()>1){
            for (int i  = 1; i<points.size(); i++){
                paintLine(points.get(i-1).getX(),points.get(i-1).getY(),points.get(i).getX(),points.get(i).getY(), color);
                paintDot(points.get(i), color);
            }
            paintDot(points.get(0), color);
        }

    }
    public void paintLine(double a1, double b1, double a2, double b2, Color color) {

        boolean ishorizontal = Math.abs(a2 - a1) >= Math.abs(b2 - b1);

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x1;
        int y1;
        int x2;
        int y2;

        if (ishorizontal) {
            if (a1 < a2) {
                x1 = (int) a1;
                y1 = (int) b1;
                x2 = (int) a2;
                y2 = (int) b2;
            } else {
                x1 = (int) a2;
                y1 = (int) b2;
                x2 = (int) a1;
                y2 = (int) b1;
            }

        } else {
            if (b1 < b2) {
                x1 = (int) b1;
                y1 = (int) a1;
                x2 = (int) b2;
                y2 = (int) a2;
            } else {
                x1 = (int) b2;
                y1 = (int) a2;
                x2 = (int) b1;
                y2 = (int) a1;
            }
        }


        int deltax = Math.abs(x1 - x2);
        int deltay = Math.abs(y1 - y2);

        int error = 0;
        int deltaerror = deltay + 1;
        int y = y1;
        int diry = y2 - y1;


        if (diry > 0) {
            diry = 1;
        } else {
            diry = -1;
        }


        for (int x = x1; x <= x2; x++) {
            if (ishorizontal) {
                pixelWriter.setColor(x, y, color);
            } else {
                pixelWriter.setColor(y, x, color);
            }

            error = error + deltaerror;
            if (error >= deltax + 1) {
                y = y + diry;
                error = error - (deltax + 1);
            }
        }


    }
}
