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
                    Line drawingLine = createLineWithStartPoint(mouseEvent.getX(), mouseEvent.getY(), 10);
                    lines.add(drawingLine);
                } else if (mouseCounter == 2) {
                    Line drawingLine = setLineEndpoint(lines.get(lines.size() - 1), mouseEvent.getX(), mouseEvent.getY());
                    double differenceX = Math.abs(drawingLine.getStartX() - drawingLine.getEndX());
                    double differenceY = Math.abs(drawingLine.getStartY() - drawingLine.getEndY());

                    drawingLine.setOnMouseDragged(event -> {
                        setLineMove(drawingLine, event.getX(), event.getY(), differenceX, differenceY);
                    });

                    borderPane.getChildren().add(drawingLine);
                    mouseCounter = 0;
                }
            }

            if (choiceBox.getValue().equals("Okrąg")) {
                if (mouseCounter == 1) {
                    Circle drawingCircle = createCircleWithStartPoint(mouseEvent.getX(), mouseEvent.getY());
                    circles.add(drawingCircle);
                } else if (mouseCounter == 2) {
                    Circle drawingCircle = setCircleRadius(circles.get(circles.size() - 1), mouseEvent.getX(), mouseEvent.getY());
                    drawingCircle.setOnMouseDragged(event -> {
                        setCircleMove(drawingCircle, event.getX(), event.getY());
                    });

                    borderPane.getChildren().add(drawingCircle);
                    mouseCounter = 0;
                }
            }

            if (choiceBox.getValue().equals("Prostokąt")) {
                if (mouseCounter == 1) {
                    Rectangle drawingRectangle = createRectangleWidthStartPoint(mouseEvent.getX(), mouseEvent.getY());
                    rectangles.add(drawingRectangle);
                } else if (mouseCounter == 2) {
                     setRectangleWidth(rectangles.get(rectangles.size() - 1), mouseEvent.getX());
                } else if (mouseCounter == 3) {
                    Rectangle drawingRectangle = setRectangleHeight(rectangles.get(rectangles.size() - 1), mouseEvent.getY());
                    drawingRectangle.setOnMouseDragged(event -> {
                      setRectangleMove(drawingRectangle, event.getX(), event.getY());
                    });

                    borderPane.getChildren().add(drawingRectangle);
                    mouseCounter = 0;
                }
            }
        });
    }

    public void setRectangleMove(Rectangle rectangle, double x, double y) {
        if (rectangle.contains(x, y) && choiceBox.getValue() == "Przeciągnij") {
            clickedShapes.add(rectangle);
        }

        if (clickedShapes.contains(rectangle)) {
            rectangle.setX(x);
            rectangle.setY(y);
        }
    }

    public Rectangle createRectangleWidthStartPoint(double x, double y) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        return rectangle;
    }

    public Rectangle setRectangleWidth(Rectangle rectangle, double x) {
        rectangle.setWidth(Math.abs(x - rectangle.getX()));
        return rectangle;
    }

    public Rectangle setRectangleHeight(Rectangle rectangle, double y) {
        rectangle.setHeight(Math.abs(y - rectangle.getY()));
        return rectangle;
    }

    public void setCircleMove(Circle circle, double x, double y) {
        if (circle.contains(x, y) && choiceBox.getValue() == "Przeciągnij") {
            clickedShapes.add(circle);
        }

        if (clickedShapes.contains(circle)) {
            circle.setCenterX(x);
            circle.setCenterY(y);
        }
    }

    public Circle createCircleWithStartPoint(double x, double y) {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        return circle;
    }

    public Circle setCircleRadius(Circle circle, double x, double y) {
        circle.setRadius(Math.sqrt(Math.pow(circle.getCenterX() - x, 2) + Math.pow(circle.getCenterY() - y, 2)));
        return circle;
    }
    public void setLineMove(Line line, double x, double y, double differenceX, double differenceY) {
        if (line.contains(x, y) && choiceBox.getValue() == "Przeciągnij") {
            clickedShapes.add(line);
        }

        if (clickedShapes.contains(line)) {
            if (line.getEndX() > line.getStartX()) line.setEndX(x + differenceX);
            else line.setEndX(x - differenceX);
            if (line.getEndY() > line.getStartY()) line.setEndY(y + differenceY);
            else line.setEndY(y - differenceY);
            line.setStartX(x);
            line.setStartY(y);
        }
    }

    public Line createLineWithStartPoint(double startX, double startY, double width) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setStrokeWidth(width);

        return line;
    }

    public Line setLineEndpoint(Line line, double x, double y) {
        line.setEndX(x);
        line.setEndY(y);
        return line;
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

        Line line = new Line(startX, startY + offset + 100, endX, endY + offset + 100);

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