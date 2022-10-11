package com.example.gkpaintprojekt;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PaintController {
    public Canvas canvas;
    public TextField brushSize;
    public TextField lineStartPointX;
    public TextField lineStartPointY;
    public TextField lineEndPointX;
    public TextField lineEndPointY;
    public Button applyLineButton;
    public TextField circleMiddleX;
    public TextField circleMiddleY;
    public TextField circleRadius;
    public Button applyCircleButton;
    public TextField rectX;
    public TextField rectY;
    public TextField rectWidth;
    public TextField rectHeight;
    public Button applyRectangle;
    public Alert alert = new Alert(Alert.AlertType.NONE);
    public BorderPane borderPane;
    public ChoiceBox choiceBox;
    public int offset = 262;
    public HashSet<Shape> clickedShapes = new HashSet<>();
    public int mouseCounter = 0;
//    public Line drawingLine;
    public List<Line> lines = new ArrayList<>();
    public List<Circle> circles = new ArrayList<>();
    public List<Rectangle> rectangles = new ArrayList<>();
//    public Rectangle drawingRectangle;
//    public Circle drawingCircle;

    public void initialize() {
        choiceBox.setItems(FXCollections.observableArrayList("Linia", "Okrąg", "Prostokąt", "Przeciągnij"));
        choiceBox.setValue("Przeciągnij");
        choiceBox.setOnAction(event -> {
            if (choiceBox.getValue() == "Linia" && mouseCounter == 1) {
                if (lines.size() > 0) lines.remove(lines.size() - 1);
            } else if (choiceBox.getValue() == "Okrąg" && mouseCounter == 1) {
                if (circles.size() > 0) circles.remove(circles.size() - 1);
            } else if (choiceBox.getValue() == "Prostokąt" && (mouseCounter == 1 || mouseCounter == 2)) {
                if (rectangles.size() > 0) rectangles.remove(rectangles.size() - 1);
            }

            mouseCounter = 0;
        });

        borderPane.setOnMouseClicked(mouseEvent -> {
            mouseCounter++;
            if (choiceBox.getValue().equals("Linia")) {
                if (mouseCounter == 1) {
                    Line drawingLine = new Line();
                    drawingLine.setStartX(mouseEvent.getX());
                    drawingLine.setStartY(mouseEvent.getY());
                    lines.add(drawingLine);
                } else if (mouseCounter == 2) {
                    Line drawingLine = lines.get(lines.size() - 1);
                    drawingLine.setEndX(mouseEvent.getX());
                    drawingLine.setEndY(mouseEvent.getY());
                    drawingLine.setStrokeWidth(10);

                    double differenceX = Math.abs(drawingLine.getStartX() - drawingLine.getEndX());
                    double differenceY = Math.abs(drawingLine.getStartY() - drawingLine.getEndY());

                    drawingLine.setOnMouseDragged(event -> {
                        if (drawingLine.contains(event.getX(), event.getY()) && choiceBox.getValue() == "Przeciągnij") {
                            clickedShapes.add(drawingLine);
                        }

                        if (clickedShapes.contains(drawingLine)) {
                            drawingLine.setStartX(event.getX());
                            drawingLine.setStartY(event.getY());
                            if (drawingLine.getEndX() > drawingLine.getStartX()) drawingLine.setEndX(event.getX() + differenceX);
                            else drawingLine.setEndX(event.getX() - differenceX);
                            drawingLine.setEndY(event.getY() - differenceY);
                        }
                    });

                    borderPane.getChildren().add(drawingLine);
                    mouseCounter = 0;
                }
            }

            if (choiceBox.getValue().equals("Okrąg")) {
                if (mouseCounter == 1) {
                    Circle drawingCircle = new Circle();
                    drawingCircle.setCenterX(mouseEvent.getX());
                    drawingCircle.setCenterY(mouseEvent.getY());
                    circles.add(drawingCircle);
                } else if (mouseCounter == 2) {
                    Circle drawingCircle = circles.get(circles.size() - 1);
                    drawingCircle.setRadius(Math.sqrt(Math.pow(drawingCircle.getCenterX() - mouseEvent.getX(), 2) + Math.pow(drawingCircle.getCenterY() - mouseEvent.getY(), 2)));
                    drawingCircle.setOnMouseDragged(event -> {
                        if (drawingCircle.contains(event.getX(), event.getY()) && choiceBox.getValue() == "Przeciągnij") {
                            clickedShapes.add(drawingCircle);
                        }

                        if (clickedShapes.contains(drawingCircle)) {
                            drawingCircle.setCenterX(event.getX());
                            drawingCircle.setCenterY(event.getY());
                        }
                    });

                    borderPane.getChildren().add(drawingCircle);
                    mouseCounter = 0;
                }
            }

            if (choiceBox.getValue().equals("Prostokąt")) {
                if (mouseCounter == 1) {
                    Rectangle drawingRectangle = new Rectangle();
                    drawingRectangle.setX(mouseEvent.getX());
                    drawingRectangle.setY(mouseEvent.getY());
                    rectangles.add(drawingRectangle);
                } else if (mouseCounter == 2) {
                    Rectangle drawingRectangle = rectangles.get(rectangles.size() - 1);
                    drawingRectangle.setWidth(Math.abs(mouseEvent.getX() - drawingRectangle.getX()));
                } else if (mouseCounter == 3) {
                    Rectangle drawingRectangle = rectangles.get(rectangles.size() - 1);
                    drawingRectangle.setHeight(Math.abs(mouseEvent.getY() - drawingRectangle.getY()));
                    drawingRectangle.setOnMouseDragged(event -> {
                        if (drawingRectangle.contains(event.getX(), event.getY()) && choiceBox.getValue() == "Przeciągnij") {
                            clickedShapes.add(drawingRectangle);
                        }

                        if (clickedShapes.contains(drawingRectangle)) {
                            drawingRectangle.setX(event.getX());
                            drawingRectangle.setY(event.getY());
                        }
                    });

                    borderPane.getChildren().add(drawingRectangle);
                    mouseCounter = 0;
                }
            }
        });
    }

    public void onApplyLineButtonClick(ActionEvent actionEvent) {
        double startX = Double.parseDouble(lineStartPointX.getText());
        double startY = Double.parseDouble(lineStartPointY.getText());
        double endX = Double.parseDouble(lineEndPointX.getText());
        double endY = Double.parseDouble(lineEndPointY.getText());
        if (startX < 0 || startY < 0 || endX < 0 || endY < 0) {
            alert.setAlertType(Alert.AlertType.ERROR);
            return;
        }

        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY + offset + 100);
        line.setEndX(endX);
        line.setEndY(endY + offset + 100);

        double differenceX = Math.abs(startX - endX);
        double differenceY = Math.abs(line.getStartY() - line.getEndY());

        line.setStrokeWidth(50);
        line.setOnMouseDragged(mouseEvent -> {
            if (line.contains(mouseEvent.getX(), mouseEvent.getY())) {
                clickedShapes.add(line);
            }

            if (clickedShapes.contains(line)) {
                line.setStartX(mouseEvent.getX());
                line.setStartY(mouseEvent.getY());
                line.setEndX(mouseEvent.getX() + differenceX);
                line.setEndY(mouseEvent.getY() + differenceY);
            }
        });

        borderPane.getChildren().add(line);
        mouseCounter = 0;
    }

    public Double getSize() {
        return Double.parseDouble(brushSize.getText());
    }

    public void onApplyCircleButtonClick(ActionEvent actionEvent) {
        double middleX = Double.parseDouble(circleMiddleX.getText());
        double middleY = Double.parseDouble(circleMiddleY.getText());
        double radius = Double.parseDouble(circleRadius.getText());

        if (middleX < 0 || middleY < 0 || radius < 0 ) {
            alert.setAlertType(Alert.AlertType.ERROR);
            return;
        }

        Circle circle = new Circle();
        circle.setCenterX(middleX);
        circle.setCenterY(middleY + offset + (radius));
        circle.setRadius(radius);
        circle.setOnMouseDragged(mouseEvent -> {
            if (circle.contains(mouseEvent.getX(), mouseEvent.getY())) {
                clickedShapes.add(circle);
            }

            if (clickedShapes.contains(circle)) {
                circle.setCenterX(mouseEvent.getX());
                circle.setCenterY(mouseEvent.getY());
            }
        });
        borderPane.getChildren().add(circle);
        mouseCounter = 0;
    }

    public void onApplyRectangleButtonClick(ActionEvent actionEvent) {
        double x = Double.parseDouble(rectX.getText());
        double y = Double.parseDouble(rectY.getText());
        double width = Double.parseDouble(rectWidth.getText());
        double height = Double.parseDouble(rectHeight.getText());
        if (x < 0 || y < 0 || width < 0 || height < 0) {
            alert.setAlertType(Alert.AlertType.ERROR);
            return;
        }

        Rectangle rect = new Rectangle();
        rect.setX(x);
        rect.setY(y + offset);
        rect.setHeight(height);
        rect.setWidth(width);
        rect.setOnMouseDragged(mouseEvent -> {
            if (rect.contains(mouseEvent.getX(), mouseEvent.getY())) {
                clickedShapes.add(rect);
            }

            if (clickedShapes.contains(rect)) {
                rect.setX(mouseEvent.getX());
                rect.setY(mouseEvent.getY());
            }
        });

        borderPane.getChildren().add(rect);
        mouseCounter = 0;
    }
}