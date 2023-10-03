package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private int workerDot = -1;

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    private ProtoCurvePainter painter;

    @FXML
    private void initialize() {
        painter = new ProtoCurvePainter(canvas.getGraphicsContext2D());

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(event);
                case SECONDARY -> handleRightClick(event);
            }
        });
    }

    private void handlePrimaryClick(MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        if (workerDot < 0) {
            points.add(clickPoint);
        } else {
            if (event.isAltDown()) {
                points.add(workerDot, clickPoint);
            } else {
                points.set(workerDot, clickPoint);
            }
            workerDot = -1;
        }


        repaint();
    }

    private void handleRightClick(MouseEvent event) {
        System.out.println(event.getX() + " " + event.getY());

        if (workerDot >= 0) {
            repaint();
            workerDot = 0;
        }


        for (int i = 0; i < points.size(); i++) {
            if ((Math.abs(points.get(i).getX() - event.getX()) <= 3) && (Math.abs(points.get(i).getY() - event.getY()) <= 3)) {

                if (event.isAltDown()) {
                    points.remove(i);
                    repaint();
                } else {
                    painter.paintDot(points.get(i), Color.RED);
                    workerDot = i;
                }

            }
        }


    }


    private void repaint() {
        painter.clear();
        if (points.size() > 1) {
            painter.paintBrokenLine(points, Color.BLACK);
            painter.paintBezye(points, Color.BLACK);
        } else if (points.size() == 1) {
            painter.paintDot(points.get(0), Color.BLACK);
        }
    }

}