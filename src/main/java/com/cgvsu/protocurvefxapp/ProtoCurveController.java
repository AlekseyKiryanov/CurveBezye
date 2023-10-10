package com.cgvsu.protocurvefxapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

     @FXML
    private ColorPicker start;

    @FXML
    private ColorPicker end;

    @FXML
    private Button clear;

    @FXML
    private Button help;
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

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                repaint();
                if (workerDot>0){
                    painter.paintDot(points.get(workerDot), Color.RED);
                }
            }
        });

        end.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                repaint();
                if (workerDot>0){
                    painter.paintDot(points.get(workerDot), Color.RED);
                }
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                points.clear();
                repaint();
            }
        });

        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Помощь");
                alert.setHeaderText("ЛКМ -> Построить точки ломаной\nПКМ -> Отметить точку для редактирования\nЛКМ -> Переместить выбранную точку\nAlt+ЛКМ -> Вставить точку левее выбранной\nAlt+ПКМ -> Удалить точку\nClear -> Очистить всё\nВыбор цвета -> Цвета начала и конца для градиентной заливки\nby ATr");
                alert.show();
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
                break;

            }
        }


    }





    private void repaint() {
        painter.clear();
        if (points.size() > 1) {
            painter.paintBrokenLine(points, Color.BLACK);
            painter.paintBezye(points, start.getValue(), end.getValue());
        } else if (points.size() == 1) {
            painter.paintDot(points.get(0), Color.BLACK);
        }
    }

}