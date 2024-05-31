package com.example.treefractals;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.EventListener;
import java.util.random.RandomGenerator;

public class FractalsController {

    @FXML
    private Button btClear;

    @FXML
    private Button btDraw;

    @FXML
    private CheckBox chkPickColor;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox chkRndColor;

    @FXML
    private Canvas myCanvas;

    @FXML
    private ComboBox<String> cmbChoice;

    @FXML
    private Spinner< Integer > spLeftBranch;

    @FXML
    private Spinner< Integer > spRightBranch;

    @FXML
    private Spinner< Double> spDuration;

    private GraphicsContext gc;

    int red, blue, green;
    double angleLeft, angleRight;
    int animationDuration;

    /*
        to do:
        1) text box per configurazione alberi esterni al programma (utilizzare un metacarattere per dividere i parametri)
        2) pulsante load per il settaggio del parametri
        3) combobox per alberi già configurati (update dei parametri anche qua, usiamo la stessa load maybe?)
           (variabili globali per i parametri, per forza!)
        4) pulizia del codice e refactoring
     */

    public void initialize() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        myCanvas.setWidth(screenWidth * 2);
        myCanvas.setHeight(screenHeight * 0.8859);

        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(myCanvas.getLayoutX(), myCanvas.getLayoutY(), myCanvas.getWidth(), myCanvas.getHeight());

        cmbChoice.setItems(FXCollections.observableArrayList("Tree Fractals"));

        disabling();

        spLeftBranch.setEditable(true);
        spLeftBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 30, 1));

        spRightBranch.setEditable(true);
        spRightBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 30, 1));

        spDuration.setEditable(true);
        spDuration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 3, 1.5, 0.1));
    }

    private void disabling() {
        btClear.setDisable(true);
        btDraw.setDisable(true);

        spLeftBranch.setDisable(true);
        spRightBranch.setDisable(true);
        spDuration.setDisable(true);

        colorPicker.setDisable(true);

        chkPickColor.setDisable(true);
        if(chkPickColor.isSelected()) {
            colorPicker.setValue(Color.WHITE);
            chkPickColor.setSelected(false);
        }

        chkRndColor.setDisable(true);
        if(chkRndColor.isSelected()) {
            chkRndColor.setSelected(false);
        }

        cmbChoice.setDisable(false);
    }

    public void onComboItemPicked(ActionEvent event) {
        if(!cmbChoice.getSelectionModel().isEmpty()) {
            String choice = cmbChoice.getSelectionModel().getSelectedItem();

            if (choice.equals("Tree Fractals")) {
                spLeftBranch.setDisable(false);
                spRightBranch.setDisable(false);
                spDuration.setDisable(false);

                chkPickColor.setDisable(false);
                chkRndColor.setDisable(false);

                cmbChoice.setDisable(true);
            }
        }
    }

    public void onRandomColorPicked(ActionEvent event) {
        red = RandomGenerator.getDefault().nextInt(255);
        blue = RandomGenerator.getDefault().nextInt(255);
        green = RandomGenerator.getDefault().nextInt(255);

        btDraw.setDisable(false);

        if(chkPickColor.isSelected()) {
            colorPicker.setValue(Color.WHITE);
            colorPicker.setDisable(true);
            chkPickColor.setSelected(false);
        }
    }

    public void onPickColorPicked(ActionEvent event) {
        btDraw.setDisable(false);
        colorPicker.setDisable(false);

        if(chkRndColor.isSelected()) {
            chkRndColor.setSelected(false);
        }
    }


    public void onClearClicked(ActionEvent event) {
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        disabling();
        cmbChoice.getSelectionModel().clearSelection();
    }

    public void onDrawClicked(ActionEvent event) {
        if ( chkPickColor.isSelected() ) {
            red = ( int ) (colorPicker.getValue().getRed() * 255);
            blue = ( int ) (colorPicker.getValue().getBlue() * 255);
            green = ( int ) (colorPicker.getValue().getGreen() * 255);
        }

        drawBranchRecursive(new Point2D(myCanvas.getWidth() / 2.033, myCanvas.getHeight() - 200),
                            -90, myCanvas.getHeight() / 7, 1, 10, 1, Color.rgb(red, green, blue));
    }

    private void drawBranchRecursive(Point2D start, double rotation, double len, double factorscale, double lineWidth, int depth, Color color) {
        drawTree(start, rotation, len, factorscale, lineWidth, depth, color);
    }

    private void drawTree(Point2D start, double rotation, double len, double factorscale, double lineWidth, int depth, Color color) {
        angleLeft = spLeftBranch.getValue();
        angleRight = spRightBranch.getValue();

        gc.setStroke(color);
        Color betterColor = calculateColor(depth, color.deriveColor(10, 1, 1, 1));

        Point2D end = findEndPoint(start, rotation, len);

        animateLine(start, end, lineWidth, factorscale, () -> {
            gc.setLineWidth(lineWidth / factorscale);
            gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

            if ( len > 4 ) {
                drawBranchRecursive(end, rotation - angleLeft, len * 0.8, factorscale / 0.6, lineWidth, depth + 1, betterColor);
                drawBranchRecursive(end, rotation + angleRight, len * 0.8, factorscale / 0.6, lineWidth, depth + 1, betterColor);
            }else{
                btClear.setDisable(false);
            }
        });
    }

    private void animateLine(Point2D start, Point2D end, double lineWidth, double factorscale, Runnable onFinish) {
        DoubleProperty lengthProperty = new SimpleDoubleProperty(0);

        animationDuration = (int) (spDuration.getValue() * 1000);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lengthProperty, 0)),
                new KeyFrame(Duration.millis(animationDuration), new KeyValue(lengthProperty, end.distance(start)))
        );

        timeline.setOnFinished(event -> onFinish.run());

        lengthProperty.addListener((observable, oldValue, newValue) -> {
            double animatedLength = newValue.doubleValue();
            Point2D animatedEnd = interpolatePoint(start, end, animatedLength / end.distance(start));

            gc.setLineWidth(lineWidth / factorscale);
            gc.strokeLine(start.getX(), start.getY(), animatedEnd.getX(), animatedEnd.getY());
        });

        timeline.play();
    }

    private Point2D interpolatePoint(Point2D start, Point2D end, double fraction) {
        double x = start.getX() + fraction * (end.getX() - start.getX());
        double y = start.getY() + fraction * (end.getY() - start.getY());
        return new Point2D(x, y);
    }

    private Color calculateColor(int depth, Color color) {
        int r = ( int ) (color.getRed() * 255);
        int b = ( int ) (color.getBlue() * 255);
        int g = ( int ) (color.getGreen() * 255);

        int alpha = 50 + (depth * 10);

        return Color.rgb(r, g, b, alpha / 255.0);
    }

    private Point2D findEndPoint(Point2D start, double rotation, double len) {
        double radians = Math.toRadians(rotation);

        double x = start.getX();
        double y = start.getY();

        x = x + (len * Math.cos(radians));
        y = y + (len * Math.sin(radians));

        return new Point2D(x, y);
    }
}