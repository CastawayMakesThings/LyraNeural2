package com.equinox.lyra2.processing;

import com.equinox.lyra2.Enums;

import static com.equinox.lyra2.Enums.activationFunctions.SIGMOID;

public class ActivationMethods {


    public static double activate(double x, Enums.activationFunctions function) {
        return switch (function) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-x));
            case TANH -> Math.tanh(x);
            case RELU -> Math.max(0, x);
            case LEAKY_RELU -> x > 0 ? x : 0.01 * x;
        };
    }

    public static double derivative(double x, Enums.activationFunctions function) {
        return switch (function) {
            case SIGMOID -> {
                double sigmoid = activate(x, Enums.activationFunctions.SIGMOID);
                yield sigmoid * (1 - sigmoid);
            }
            case TANH -> 1 - Math.pow(Math.tanh(x), 2);
            case RELU -> x > 0 ? 1.0 : 0.0;
            case LEAKY_RELU -> x > 0 ? 1.0 : 0.01;
        };
    }
}
