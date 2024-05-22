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

    Timeline timeline;

    int red, blue, green;

    public void initialize() {

        myCanvas.setWidth(Screen.getPrimary().getBounds().getWidth() * 2);
        myCanvas.setHeight(Screen.getPrimary().getBounds().getHeight() * 0.89);

        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(myCanvas.getLayoutX(), myCanvas.getLayoutY(), myCanvas.getWidth(), myCanvas.getHeight());

        cmbChoice.setItems(FXCollections.observableArrayList("Tree Fractals", "Snowflake"));

        btClear.setDisable(true);
        btDraw.setDisable(true);

        spLeftBranch.setDisable(true);
        spRightBranch.setDisable(true);

        colorPicker.setDisable(true);

        chkPickColor.setDisable(true);
        chkRndColor.setDisable(true);

        spLeftBranch.setEditable(true);
        spLeftBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 30, 1));

        spRightBranch.setEditable(true);
        spRightBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 30, 1));

        spDuration.setEditable(true);
        spDuration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 3, 1.5, 0.1));

    }

    public void onComboItemPicked(ActionEvent event) {

        String choice = cmbChoice.getValue();

        if (choice.equals("Tree Fractals")) {
            spLeftBranch.setDisable(false);
            spRightBranch.setDisable(false);
            // btDraw.setDisable(false);

            chkPickColor.setDisable(false);
            chkRndColor.setDisable(false);

            cmbChoice.setDisable(true);
        }
        else if (choice.equals("Snowflake")) {
            //btDraw.setDisable(false);

            chkPickColor.setDisable(false);
            chkRndColor.setDisable(false);

            cmbChoice.setDisable(true);
        }

    }

    public void onRandomColorPicked(ActionEvent event) {
        red = RandomGenerator.getDefault().nextInt(255);
        blue = RandomGenerator.getDefault().nextInt(255);
        green = RandomGenerator.getDefault().nextInt(255);

        btDraw.setDisable(false);
        chkPickColor.setDisable(true);
    }


    public void onPickColorPicked(ActionEvent event) {

        btDraw.setDisable(false);
        colorPicker.setDisable(false);
        chkRndColor.setDisable(true);
    }


    public void onClearClicked(ActionEvent event) {

        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        initialize();

    }




    public void onDrawClicked(ActionEvent event) {

        if ( chkPickColor.isSelected() ) {
            red = ( int ) (colorPicker.getValue().getRed() * 255);
            blue = ( int ) (colorPicker.getValue().getBlue() * 255);
            green = ( int ) (colorPicker.getValue().getGreen() * 255);
        }

        drawBranchRecursive(new Point2D(myCanvas.getWidth() / 2, myCanvas.getHeight() - 200),
                            -90, myCanvas.getHeight() / 7, 1, 10, 1, Color.rgb(red, green, blue));

        // drawBranchesIterative(new Point2D(myCanvas.getWidth() / 2, myCanvas.getHeight() - 200),
        //                   -90, myCanvas.getHeight() / 7, 1, 10, Color.rgb(red, green, blue));
    }



    private void drawBranchRecursive(Point2D start, double rotation, double len, double factorscale, double lineWidth, int depth, Color color) {

        drawTree(start, rotation, len, factorscale, lineWidth, depth, color);

        // drawTree(start.add(5, 5), rotation, len, factorscale, lineWidth, depth, Color.DARKGRAY);

        // Aggiungi luci
    }

    private void drawTree(Point2D start, double rotation, double len, double factorscale, double lineWidth, int depth, Color color) {
        // Il tuo codice per disegnare l'albero qui...
        double angleLeft = spLeftBranch.getValue();
        double angleRight = spRightBranch.getValue();

        gc.setStroke(color);
        Color betterColor = calculateColor(depth, color.deriveColor(10, 1, 1, 1));

        // Calcola la posizione finale della linea
        Point2D end = findEndPoint(start, rotation, len);

        // Inizia l'animazione della crescita della linea
        animateLine(start, end, lineWidth, factorscale, () -> {
            // Quando l'animazione della linea è completata, disegna la linea
            gc.setLineWidth(lineWidth / factorscale);
            gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

            // Se la lunghezza è sufficientemente grande, continua a disegnare i rami
            if ( len > 4 ) {
                drawBranchRecursive(end, rotation - angleLeft, len * 0.8, factorscale / 0.6, lineWidth, depth + 1, betterColor);
                drawBranchRecursive(end, rotation + angleRight, len * 0.8, factorscale / 0.6, lineWidth, depth + 1, betterColor);
            }
        });

        btClear.setDisable(false);
    }


    private void animateLine(Point2D start, Point2D end, double lineWidth, double factorscale, Runnable onFinish) {
        DoubleProperty lengthProperty = new SimpleDoubleProperty(0);

        // Durata dell'animazione in millisecondi
        int animationDuration = (int) (spDuration.getValue() * 1000);

        // Crea una timeline per animare la crescita della linea
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lengthProperty, 0)),
                new KeyFrame(Duration.millis(animationDuration), new KeyValue(lengthProperty, end.distance(start)))
        );

        // Imposta l'azione da eseguire alla fine dell'animazione
        timeline.setOnFinished(event -> onFinish.run());

        // Aggiorna continuamente la lunghezza della linea durante l'animazione
        lengthProperty.addListener((observable, oldValue, newValue) -> {
            double animatedLength = newValue.doubleValue();
            Point2D animatedEnd = interpolatePoint(start, end, animatedLength / end.distance(start));

            gc.setLineWidth(lineWidth / factorscale);
            gc.strokeLine(start.getX(), start.getY(), animatedEnd.getX(), animatedEnd.getY());
        });

        // Avvia l'animazione
        timeline.play();
    }


    // Interpolazione lineare tra due punti
    private Point2D interpolatePoint(Point2D start, Point2D end, double fraction) {
        double x = start.getX() + fraction * (end.getX() - start.getX());
        double y = start.getY() + fraction * (end.getY() - start.getY());
        return new Point2D(x, y);
    }


    private Color calculateColor(int depth, Color color) {
        // Calcola un colore più acceso in base alla profondità del ramo

        int r = ( int ) (color.getRed() * 255);
        int b = ( int ) (color.getBlue() * 255);
        int g = ( int ) (color.getGreen() * 255);

        int alpha = 50 + (depth * 10); // Diminuisce l'opacità man mano che la profondità aumenta

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