package com.example.model;

public class HornerScheme {

    public double horner(double[] factors, int degree, double x) {
        if (degree == 0) {
            return factors[0];
        }
        return x * horner(factors, degree - 1, x) + factors[degree];
    }

}
