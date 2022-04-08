package com.example.model;

public class HornerScheme {

    public double horner(double[] factors, int degree, double x) {
        double hornerResult = factors[0];
        for (int i = 1; i < degree; i++) {
            hornerResult = hornerResult * x + factors[i];
        }
        return hornerResult;
    }

}
