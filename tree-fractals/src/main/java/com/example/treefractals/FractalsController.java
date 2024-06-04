package com.example.treefractals;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
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
    private Button btLoad;

    @FXML
    private CheckBox chkPickColor;

    @FXML
    private CheckBox chkRndColor;

    @FXML
    private ComboBox< String > cmbChoice;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Canvas myCanvas;

    @FXML
    private Spinner< Double > spDuration;

    @FXML
    private Spinner< Integer > spLeftBranch;

    @FXML
    private Spinner< Integer > spRightBranch;

    @FXML
    private TextField txtUserFractal;

    private GraphicsContext gc;

    private int red, blue, green;
    private int angleLeft, angleRight;
    private Alert alert;


    public void initialize() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        myCanvas.setWidth(screenWidth * 2);
        myCanvas.setHeight(screenHeight * 0.88589);

        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(myCanvas.getLayoutX(), myCanvas.getLayoutY(), myCanvas.getWidth(), myCanvas.getHeight());

        cmbChoice.setItems(FXCollections.observableArrayList("15-15 GREEN", "30-30 SIENNA", "90-90 OLIVE",
                                                             "30-130 PURPLE", "120-60 YELLOW", "105-75 BLUE",
                                                             "19-89 PINK", "82-8 FUCHSIA", "12-26 SALMON",
                                                             "69-45 TURQUOISE"));

        settings();

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Usage Information");
        alert.setHeaderText(null);
        alert.setContentText("If you have any issue with how this app works make sure to check the README file!");
        alert.showAndWait();

        spLeftBranch.setEditable(true);
        spLeftBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 0, 1));

        spRightBranch.setEditable(true);
        spRightBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 0, 1));

        spDuration.setEditable(true);
        spDuration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 3, 1, 0.1));
    }

    private void settings() {
        btClear.setDisable(true);
        btDraw.setDisable(true);
        btLoad.setDisable(false);

        chkPickColor.setDisable(false);
        chkRndColor.setDisable(false);
        txtUserFractal.setDisable(false);
        spLeftBranch.setDisable(false);
        spRightBranch.setDisable(false);
        spDuration.setDisable(false);
        cmbChoice.setDisable(false);

        colorPicker.setDisable(true);

        txtUserFractal.setText("");

        if ( chkPickColor.isSelected() ) {
            colorPicker.setValue(Color.WHITE);
            chkPickColor.setSelected(false);
        }

        if ( chkRndColor.isSelected() ) {
            chkRndColor.setSelected(false);
        }
    }

    public void onComboItemPicked() {
        if ( !cmbChoice.getSelectionModel().isEmpty() ) {
            String choice = cmbChoice.getSelectionModel().getSelectedItem();

            if ( choice.equals("30-30 SIENNA") ) {
                spLeftBranch.getValueFactory().setValue(30);
                spRightBranch.getValueFactory().setValue(30);
                colorPicker.setValue(Color.SIENNA);

            }
            else if ( choice.equals("15-15 GREEN") ) {
                spLeftBranch.getValueFactory().setValue(15);
                spRightBranch.getValueFactory().setValue(15);
                colorPicker.setValue(Color.GREEN);

            }
            else if ( choice.equals("30-130 PURPLE") ) {
                spLeftBranch.getValueFactory().setValue(30);
                spRightBranch.getValueFactory().setValue(130);
                colorPicker.setValue(Color.PURPLE);

            }
            else if ( choice.equals("60-120 YELLOW") ) {
                spLeftBranch.getValueFactory().setValue(60);
                spRightBranch.getValueFactory().setValue(120);
                colorPicker.setValue(Color.YELLOW);

            }
            else if ( choice.equals("105-75 BLUE") ) {
                spLeftBranch.getValueFactory().setValue(105);
                spRightBranch.getValueFactory().setValue(75);
                colorPicker.setValue(Color.BLUE);

            }
            else if ( choice.equals("19-89 PINK") ) {
                spLeftBranch.getValueFactory().setValue(19);
                spRightBranch.getValueFactory().setValue(89);
                colorPicker.setValue(Color.PINK);

            }
            else if ( choice.equals("82-8 FUCHSIA") ) {
                spLeftBranch.getValueFactory().setValue(82);
                spRightBranch.getValueFactory().setValue(8);
                colorPicker.setValue(Color.FUCHSIA);

            }
            else if ( choice.equals("12-26 SALMON") ) {
                spLeftBranch.getValueFactory().setValue(12);
                spRightBranch.getValueFactory().setValue(26);
                colorPicker.setValue(Color.SALMON);

            }
            else if ( choice.equals("69-45 TURQUOISE") ) {
                spLeftBranch.getValueFactory().setValue(69);
                spRightBranch.getValueFactory().setValue(45);
                colorPicker.setValue(Color.TURQUOISE);

            }
            else if ( choice.equals("90-90 OLIVE") ) {
                spLeftBranch.getValueFactory().setValue(90);
                spRightBranch.getValueFactory().setValue(90);
                colorPicker.setValue(Color.OLIVE);
            }

            chkPickColor.setSelected(true);
            colorPicker.setDisable(false);
            btDraw.setDisable(false);
            btClear.setDisable(true);
        }
    }

    public void onRandomColorPicked() {
        red = RandomGenerator.getDefault().nextInt(255);
        blue = RandomGenerator.getDefault().nextInt(255);
        green = RandomGenerator.getDefault().nextInt(255);

        btDraw.setDisable(false);

        if ( chkPickColor.isSelected() ) {
            colorPicker.setValue(Color.WHITE);
            colorPicker.setDisable(true);
            chkPickColor.setSelected(false);
        }
    }

    public void onPickColorPicked() {
        btDraw.setDisable(false);
        colorPicker.setDisable(false);

        if ( chkRndColor.isSelected() ) {
            chkRndColor.setSelected(false);
        }
    }

    public void onLoadClicked() {
        String[] userParams = txtUserFractal.getText().split("\\.");
        int duration = 0;

        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Invalid string inserted!");
        alert.setHeaderText(null);
        alert.setHeight(175);
        alert.setWidth(500);

        if ( userParams.length != 4 ) {

           alert.setContentText("""
                                    Number of params wrong!
                                    Please insert exactly 4 params!
                                """);
           alert.showAndWait();
        }
        else{
            try {
                Color c = Color.valueOf(userParams[0].toUpperCase());
                chkPickColor.setSelected(true);
                colorPicker.setValue(c);
                colorPicker.setDisable(false);

                angleLeft = Integer.parseInt(userParams[1]);
                angleRight = Integer.parseInt(userParams[2]);

                duration = Integer.parseInt(userParams[3]);
            }
            catch ( Exception e ) {
                alert.setContentText("""
                                     Wrong string format!
                                     An example of valid string: GREEN.20.50.2000
                                 """);
                alert.showAndWait();
                return;
            }


            if ( (angleLeft < 0 || angleLeft > 150) || (angleRight < 0 || angleRight > 150) || (duration < 1000 || duration > 3000) ) {

                alert.setContentText("""
                                     Params out of bounds!
                                     Second and third param must be a value between 0 and 150 (included)!
                                     Fourth param must be a value between 1000 and 3000 (included)!
                                 """);
                alert.showAndWait();
                return;
            }
            else {
                spLeftBranch.getValueFactory().setValue(angleLeft);
                spRightBranch.getValueFactory().setValue(angleRight);
                spDuration.getValueFactory().setValue(( double ) duration / 1000);
                btDraw.setDisable(false);
            }
        }

    }

    public void onClearClicked() {
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        settings();
        cmbChoice.getSelectionModel().clearSelection();
    }

    public void onDrawClicked() {

        chkPickColor.setDisable(true);
        chkRndColor.setDisable(true);
        colorPicker.setDisable(true);
        txtUserFractal.setDisable(true);
        spLeftBranch.setDisable(true);
        spRightBranch.setDisable(true);
        spDuration.setDisable(true);
        btLoad.setDisable(true);
        cmbChoice.setDisable(true);

        if ( chkPickColor.isSelected() ) {
            red = ( int ) (colorPicker.getValue().getRed() * 255);
            blue = ( int ) (colorPicker.getValue().getBlue() * 255);
            green = ( int ) (colorPicker.getValue().getGreen() * 255);
        }

        drawBranch(new Point2D(myCanvas.getWidth() / 2.033, myCanvas.getHeight() - 230),
                   -90, myCanvas.getHeight() / 7, 1, 10, 1, Color.rgb(red, green, blue));
    }

    private void drawBranch(Point2D start, double rotation, double len, double factorScale, double lineWidth, int depth, Color color) {
        angleLeft = spLeftBranch.getValue();
        angleRight = spRightBranch.getValue();

        gc.setStroke(color);
        Color newColor = calculateColor(depth, color.deriveColor(15, 1, 1, 1));

        Point2D end = findEndPoint(start, rotation, len);

        animateLine(start, end, lineWidth, factorScale, () -> {
            gc.setLineWidth(lineWidth / factorScale);
            gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

            if ( len > 4 ) {
                drawBranch(end, rotation - angleLeft, len * 0.8, factorScale / 0.6, lineWidth, depth + 1, newColor);
                drawBranch(end, rotation + angleRight, len * 0.8, factorScale / 0.6, lineWidth, depth + 1, newColor);
            }
            else {
                btClear.setDisable(false);
                btDraw.setDisable(true);

            }
        });
    }

    private void animateLine(Point2D start, Point2D end, double lineWidth, double factorScale, Runnable onFinish) {
        DoubleProperty lengthProperty = new SimpleDoubleProperty(0);

        int animationDuration = ( int ) (spDuration.getValue() * 1000);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lengthProperty, 0)),
                new KeyFrame(Duration.millis(animationDuration), new KeyValue(lengthProperty, end.distance(start)))
        );

        timeline.setOnFinished(event -> onFinish.run());

        lengthProperty.addListener((observable, oldValue, newValue) -> {
            double animatedLength = newValue.doubleValue();
            Point2D animatedEnd = interpolatePoint(start, end, animatedLength / end.distance(start));

            gc.setLineWidth(lineWidth / factorScale);
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