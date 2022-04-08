package com.example.model;

public class Function {

    //y = 3x+1
    public static double linear(double x) {
        return 3 * x + 1;
    }

    //y = |x|
    public static double absolute(double x) {
        return Math.abs(x);
    }

    // y = 6x^5+2x^3+x^2-8
    public static double polynomial(double x) {
        return (6 * Math.pow(x, 5)) + (2 * Math.pow(x, 3)) + Math.pow((x-1), 2) - 1;
    }

    // y = 3sin(x)+5tg(x)+cos(x)
    public static double trigonometric(double x) {
        return 3 * Math.sin(x) + 5 + Math.tan(x) + Math.cos(x);
    }

    // y = 8x+|sin(2x)+1|+x^3*tg(|x|)
    public static double mixed(double x) {
        return 8 * x + Math.abs(Math.sin(2 * x) + 1) + Math.pow(x, 3) * Math.tan(Math.abs(x));
    }
}
