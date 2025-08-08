package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.Enums;

/**
 * Provides implementations of common neural network activation functions and their derivatives.
 * This utility class supports the core activation functions used in neural networks:
 * - Sigmoid: For binary classification and probability outputs (0 to 1)
 * - Tanh: For normalized outputs (-1 to 1)
 * - ReLU: For deep networks, addressing vanishing gradient problems
 * - Leaky ReLU: Modified ReLU preventing "dying neuron" problem
 */
public class ActivationMethods {

    /**
     * Applies the specified activation function to the input value.
     *
     * Function Specifications:
     * - SIGMOID: f(x) = 1 / (1 + e^(-x))
     *   Range: (0,1), Useful for: Binary classification, probability outputs
     *
     * - TANH: f(x) = tanh(x)
     *   Range: (-1,1), Useful for: Normalized outputs, hidden layers
     *
     * - RELU: f(x) = max(0,x)
     *   Range: [0,∞), Useful for: Deep networks, sparse activation
     *
     * - LEAKY_RELU: f(x) = x if x > 0 else 0.01x
     *   Range: (-∞,∞), Useful for: Preventing "dying ReLU" problem
     *
     * @param x The input value
     * @param function The activation function to apply
     * @return The result of applying the activation function
     */
    public static double activate(double x, Enums.activationFunctions function) {
        return switch (function) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-x));
            case TANH -> Math.tanh(x);
            case RELU -> Math.max(0, x);
            case LEAKY_RELU -> x > 0 ? x : 0.01 * x;
        };
    }

    /**
     * Computes the derivative of the specified activation function at the given point.
     * These derivatives are essential for backpropagation during network training.
     *
     * Derivative Specifications:
     * - SIGMOID: f'(x) = f(x)(1 - f(x))
     *   Where f(x) is the sigmoid function
     *
     * - TANH: f'(x) = 1 - tanh²(x)
     *   Derivative range: (0,1)
     *
     * - RELU: f'(x) = 1 if x > 0 else 0
     *   Binary derivative for positive/negative inputs
     *
     * - LEAKY_RELU: f'(x) = 1 if x > 0 else 0.01
     *   Small positive gradient for negative inputs
     *
     * @param x The input value
     * @param function The activation function whose derivative should be computed
     * @return The derivative value at the given point
     */
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