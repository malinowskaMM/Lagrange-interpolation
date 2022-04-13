package com.example.view;
import com.example.model.Function;
import com.example.model.Interpolation;
import com.example.model.Nodes;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class HelloController {
    @FXML private Label resultLabel;
    //@FXML private LineChart<Double, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private TextField startIntervalInput;
    @FXML private TextField endIntervalInput;
    @FXML private TextField numberOfNodesInput;
    @FXML private RadioButton mixed;
    @FXML private RadioButton absolute;
    @FXML private RadioButton linear;
    @FXML private RadioButton polynomial;
    @FXML private RadioButton trigonometric;
    @FXML private CheckBox originalFunction;
    @FXML private CheckBox interpolationFunction;
    @FXML private CheckBox interpolationNodes;
    @FXML private Button graph;


    String function;
    Integer numberOfNodes;
    double[] nodes;
    Interpolation interpolation = new Interpolation();
    XYChart.Series nodesSeries;
    XYChart.Series originalSeries;
    XYChart.Series interpolationSeries;

    @FXML
    private void initialize() {
        originalFunction.setSelected(true);
        interpolationFunction.setSelected(true);
        interpolationNodes.setSelected(true);
    }

    @FXML
    protected void onGraphButtonPressed() {
        double firstPoint = 0;
        double lastPoint = 0;
        if (startIntervalInput.getText().matches("^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")) {
            firstPoint = Double.parseDouble(startIntervalInput.getText());
        } else {
            openWarningDialog("Zły format podczas wprowadzania poczatku przedziału");
        }
        if (endIntervalInput.getText().matches("^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")) {
            lastPoint = Double.parseDouble(endIntervalInput.getText());
        } else {
            openWarningDialog("Zły format podczas wprowadzania końca przedziału");
        }
        if(firstPoint > lastPoint) {
            double temp = firstPoint;
            firstPoint = lastPoint;
            lastPoint = temp;
        }
        function = chooseFunctionByRadioButton();
        int resolution = 500;
        double[] x = new double[resolution];
        double[] y = new double[resolution];
        double[] xPosNodes = nodes;
        double [] yPosNodes = calculateValues(xPosNodes, function);

        if(interpolationNodes.isSelected()) {
            nodesSeries = interpolationNodesChecked(xPosNodes, yPosNodes);
        }
        if(originalFunction.isSelected()) {
            originalSeries = originalFunctionChecked(function, firstPoint, lastPoint, resolution, x, y);
        }
        if(interpolationFunction.isSelected()) {
            interpolationSeries = interpolationChecked(firstPoint, lastPoint, resolution, xPosNodes, yPosNodes, x, y);
        }
        LineChart<Number, Number> lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.getData().addAll(nodesSeries, originalSeries, interpolationSeries);

        Scene scene = new Scene(lineChart, 500, 400);
        scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series originalFunctionChecked(String function, double firstPoint, double lastPoint,
                                         double resolution, double[] x, double[] y) {
        double xIncrement = (lastPoint - firstPoint) / resolution;
        double p = firstPoint;
        XYChart.Series seriesOriginal =  new XYChart.Series();
        seriesOriginal.setName("Original function");
        for (int i = 0; i < resolution; i++) {
            x[i] = p;
            switch (function) {
                case "polynomial" -> y[i] = Function.polynomial(p);
                case "trigonometric" -> y[i] = Function.trigonometric(p);
                case "mixed" -> y[i] = Function.mixed(p);
                case "linear" -> y[i] = Function.linear(p);
                case "absolute" -> y[i] = Function.absolute(p);
                default -> y[i] = 0;
            }
            seriesOriginal.getData().add(new XYChart.Data(p, y[i]));
            p += xIncrement;
        }
        return seriesOriginal;

//        Graph graph = new Graph(x, y);
//        lineChart.getData().add(graph.createSeries());
        //lineChart.getScene().getStylesheets().add(HelloController.class.getResource("chart.css").toExternalForm());
    }

    private XYChart.Series interpolationChecked( double firstPoint, double lastPoint, double resolution,
                                       double[] xPos, double[] yPos, double[] x, double[] y) {
        double xIncrement = (lastPoint - firstPoint) / resolution;
        double p = firstPoint;
        XYChart.Series seriesInterpolation =  new XYChart.Series();
        seriesInterpolation.setName("Interpolation function");
        for (int i = 0; i < resolution; i++) {
            x[i] = p;
            y[i] = interpolation.calculateInterpolation(xPos, yPos, p);
            seriesInterpolation.getData().add(new XYChart.Data(p, y[i]));
            p += xIncrement;
        }
        return seriesInterpolation;
//        Graph graph = new Graph(x, y);
//        lineChart.getData().add(graph.createSeries());
        //lineChart.getScene().getStylesheets().add(HelloController.class.getResource("chart.css").toExternalForm());
    }

    private XYChart.Series interpolationNodesChecked(double[] xPos, double[] yPos) {
        XYChart.Series seriesNodes =  new XYChart.Series();
        seriesNodes.setName("Nodes");
        for( int i = 0; i < xPos.length; i++) {
            seriesNodes.getData().add(new XYChart.Data(xPos[i], yPos[i]));
        }
        return seriesNodes;
//        Graph graph = new Graph(xPos, yPos);
//        XYChart.Series<Double, Double> series = graph.createSeries();
//        lineChart.getData().add(series);
        //lineChart.getScene().getStylesheets().add(HelloController.class.getResource("chart.css").toExternalForm());
    }

    private String chooseFunctionByRadioButton() {
        if (polynomial.isSelected()) {
            return "polynomial";
        }
        if (trigonometric.isSelected()) {
            return "trigonometric";
        }
        if (mixed.isSelected()) {
            return "mixed";
        }
        if (linear.isSelected()) {
            return "linear";
        }
        if (absolute.isSelected()) {
            return "absolute";
        } else {
            openWarningDialog("Nie wybrano rodzaju funkcji");
            return "";
        }
    }

    private void openWarningDialog(String text) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Uwaga");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    @FXML
    protected void loadNodesPositionsFromFile() throws IOException {
        if (numberOfNodesInput.getText().isEmpty()){
            openWarningDialog("Przed wczytaniem pozycji węzłów, ustaw ich ilość");
        }
        if(numberOfNodesInput.getText().matches("[0-9]+")) {
            numberOfNodes = Integer.valueOf(numberOfNodesInput.getText());
            File inputFile = new File("nodePosition.txt");
            nodes = Nodes.initNodeListFromTxtFile(inputFile, numberOfNodes);
            if(nodes == null) {
                openWarningDialog("Nie udalo sie wczytac pozycji węzłów");
            } else {
                openWarningDialog("Położenia węzłów są następujące: " + Arrays.toString(nodes));
            }
        } else {
            openWarningDialog("Nie własciwa zawartość pola");
        }
    }


    private double[] calculateValues(double[] xPos, String function){
        double[] yPos = new double[xPos.length];
        switch(function) {
            case "polynomial":
                for(int i = 0; i < xPos.length; i++) {
                    yPos[i] = Function.polynomial(xPos[i]);
                }
                break;
            case "linear":
                for(int i = 0; i < xPos.length; i++) {
                    yPos[i] = Function.linear(xPos[i]);
                }
                break;
            case "absolute":
                for(int i = 0; i < xPos.length; i++) {
                    yPos[i] = Function.absolute(xPos[i]);
                }
                break;
            case "trigonometric":
                for(int i = 0; i < xPos.length; i++) {
                    yPos[i] = Function.trigonometric(xPos[i]);
                }
                break;
            case "mixed":
                for(int i = 0; i < xPos.length; i++) {
                    yPos[i] = Function.mixed(xPos[i]);
                }
                break;
        }
        return yPos;
    }

}