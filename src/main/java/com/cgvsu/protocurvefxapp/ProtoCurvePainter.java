package com.cgvsu.protocurvefxapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ProtoCurvePainter {

    private final GraphicsContext graphicsContext;

    public ProtoCurvePainter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public void paintDot(double a, double b, Color color){

        int x = (int) a;
        int y = (int) b;

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int dotSize = 3;
        for (int row = y - dotSize /2 - 1; row < y + dotSize; ++row)
            for (int col = x - dotSize /2 - 1; col < x + dotSize; ++col)
                pixelWriter.setColor(col, row, color);
    }
}
