package com.example.view;
import com.example.model.Function;
import com.example.model.Interpolation;
import com.example.model.Nodes;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Stream;


public class HelloController {
    @FXML private Label resultLabel;
    @FXML private LineChart<Double, Double> lineChart;
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

    String function;
    Integer numberOfNodes;
    double[] nodes;
    Interpolation interpolation = new Interpolation();

    @FXML
    private void initialize() {
        lineChart.setAnimated(true);

    }

    @FXML
    protected void onGraphButtonPressed() {
        // trzeba tu jakos dostarczyc nasza funkcje, roboczo wpisana z palca nizej
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
        lineChart.getData().clear();
        xAxis.setLowerBound(firstPoint);
        xAxis.setUpperBound(lastPoint);
        double[] xPosNodes = nodes;
        double [] yPosNodes = calculateValues(xPosNodes, function);
        System.out.println(Arrays.toString(xPosNodes));
        System.out.println(Arrays.toString(yPosNodes));
        if(originalFunction.isSelected()) {
            orginalFunctionChecked(function, firstPoint, lastPoint, resolution, x, y);
        }
        if(interpolationFunction.isSelected()) {
            interpolationChecked(firstPoint, lastPoint, resolution, xPosNodes, yPosNodes, x, y);
        }


    }

    private void orginalFunctionChecked( String function, double firstPoint, double lastPoint,
                                         double resolution, double[] x, double[] y) {
        double xIncrement = (lastPoint - firstPoint) / resolution;
        double p = firstPoint;
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
            p += xIncrement;
            System.out.println("Funkcja: [" + x[i] + ", " + y[i] + "]");
        }
        Graph graph = new Graph(x, y);
        lineChart.getData().add(graph.createSeries());
    }

    private void interpolationChecked( double firstPoint, double lastPoint, double resolution,
                                       double[] xPos, double[] yPos, double[] x, double[] y) {
        double xIncrement = (lastPoint - firstPoint) / resolution;
        double p = firstPoint;
        for (int i = 0; i < resolution; i++) {

            x[i] = p;
            y[i] = interpolation.calculateInterpolation(xPos, yPos, p);
            p += xIncrement;
            System.out.println("Interpolacja: [" + x[i] + ", " + y[i] + "]");
        }
        Graph graph = new Graph(x, y);
        lineChart.getData().add(graph.createSeries());

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