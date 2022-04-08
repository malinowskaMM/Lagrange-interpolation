package com.example.model;

import java.util.Vector;

public class Interpolation {

    public void calculateInterpolation(double[] x, double[] y, int numberOfNodes, double firstPoint,
                                           double lastPoint, Vector<Double> iX, Vector<Double> iY) {
        for(double i = firstPoint; i < lastPoint; i += 0.01) {
            double yVariable = 0;
            for(int j = 0; j < numberOfNodes; j++) {
                double tempY = y[j];
                for(int k = 0; k < numberOfNodes; k++) {
                    if( j != k ) {
                        tempY *= (i-x[k])/(x[j]-x[k]);
                    }
                    yVariable += tempY;
                }
                iX.add(i);
                iY.add(yVariable);
            }
        }
    }
}
