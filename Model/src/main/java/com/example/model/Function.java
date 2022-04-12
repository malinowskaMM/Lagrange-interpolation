package com.example.model;

public class Function {
    static HornerScheme hornerScheme = new HornerScheme();

    //y = 3x+1
    public static double linear(double x) {
        return 3 * x + 1;
    }

    //y = |x|
    public static double absolute(double x) {
        return Math.abs(x);
    }

    // y = x^5+x^4-2x^3-3 - najlepszy przedział (-2; 1,5)
    public static double polynomial(double x) {
        double[] factors = {1,1,-2,0,0,-3};
        return hornerScheme.horner(factors,factors.length, x );
    }

    // y = 3sin(x)+cos(2x+1) - najlepszy przedział (-3; 4)
    public static double trigonometric(double x) {
        return 3 * Math.sin(x) + Math.cos(2*x+1);
    }

    // y = 8|sin(x)|-2x^2-3x - najlepszy przedział (-3; 2)
    public static double mixed(double x) {
        return 8 * Math.abs(Math.sin(x)) - 2 * Math.pow(x, 2) - 3 * x;
    }
}
