package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.Enums;

public class ActivationMethods {

    //This method returns the value of the inputted function applied to x;
    public static double activate(double x, Enums.activationFunctions function) {
        return switch (function) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-x));
            case TANH -> Math.tanh(x);
            case RELU -> Math.max(0, x);
            case LEAKY_RELU -> x > 0 ? x : 0.01 * x;
        };
    }

    //This is the same as the last method, but finds the derivative.
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

//This class is a very simple utility class that makes it a little easier to work with activation functions.
//As you can see, there are only 4 different activation functions, so I am considering using lambdas to let
//the user write their own activation function, but that probably wont be added until later updates.


//Equinox Electronic