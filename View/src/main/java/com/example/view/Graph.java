package com.example.view;

import javafx.scene.chart.XYChart;

public class Graph {

    double[] x;
    double[] y;

    public Graph(double[] xPoints, double[] yPoints) {
        x = xPoints;
        y = yPoints;
    }

    public XYChart.Series<Double, Double> createSeries() {
        int numOfPoints = x.length;

        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < numOfPoints; i++) {
            series.getData().add(new XYChart.Data<Double, Double>(x[i], y[i]));
        }

        return series;
    }
}