package com.example.model;

import java.util.Vector;

public class Interpolation {

    // x - położenia węzłow interpolacyjnych
    // y - obliczone wartości dla położeń węzłów interpolacyjnych
    public double calculateInterpolation(double[] x, double[] y, double xValue) {
        double tempY;
        double yValue = 0;

        for (int j = 0; j < x.length; j++) {
            tempY = 1.0;
            for (int k = 0; k < x.length; k++) {
                if (j != k) {
                    tempY *= ((xValue - x[k]) / (x[j] - x[k]));
                }
            }
            yValue += tempY*y[j];
        }
        return yValue;
    }
}
