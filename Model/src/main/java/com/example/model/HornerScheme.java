package com.example.model;

public class HornerScheme {

    int horner(int[] factors, int degree, int x) {
        if (degree == 0) {
            return factors[0];
        }
        return x * horner(factors, degree - 1, x) + factors[degree];
    }

}
