package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;

/**
 * Utility class for calculating various performance metrics in machine learning models.
 * Provides methods to evaluate model performance through accuracy and mean squared error calculations.
 */
public class MetricsCalculator {

    /**
     * Calculates the accuracy of predictions compared to actual values.
     * Accuracy is determined by counting predictions that are within 0.5 of their actual values,
     * then dividing by the total number of predictions.
     *
     * @param predicted A 2D ArrayList containing the model's predicted values
     * @param actual A 2D ArrayList containing the actual (target) values
     * @return The accuracy as a ratio between 0.0 and 1.0
     * @throws IllegalArgumentException if the dimensions of predicted and actual arrays don't match
     */
    public static double calculateAccuracy(ArrayList<ArrayList<Double>> predicted,
                                           ArrayList<ArrayList<Double>> actual) {
        if (predicted.size() != actual.size()) {
            throw new IllegalArgumentException("Predicted and actual sizes must match");
        }

        // Initialize counters for correct predictions and total samples
        int correct = 0;
        int total = 0;

        // Compare predicted and actual values element by element
        for (int i = 0; i < predicted.size(); i++) {
            for (int j = 0; j < predicted.get(i).size(); j++) {
                if (Math.abs(predicted.get(i).get(j) - actual.get(i).get(j)) < 0.5) {
                    correct++; // Increment if prediction is within threshold
                }
                total++; // Track total number of samples
            }
        }

        return (double) correct / total;
    }

    /**
     * Calculates the Mean Squared Error (MSE) between predicted and actual values.
     * MSE is computed as the average of squared differences between predicted and actual values.
     * The formula used is: MSE = (1/n) * Σ(predicted - actual)²
     * where n is the total number of predictions.
     *
     * @param predicted A 2D ArrayList containing the model's predicted values
     * @param actual A 2D ArrayList containing the actual (target) values
     * @return The mean squared error value
     * @throws IllegalArgumentException if the dimensions of predicted and actual arrays don't match
     */
    public static double calculateMSE(ArrayList<ArrayList<Double>> predicted,
                                      ArrayList<ArrayList<Double>> actual) {
        if (predicted.size() != actual.size()) {
            throw new IllegalArgumentException("Predicted and actual sizes must match");
        }

        // Initialize variables for sum of squared errors and total samples
        double sumSquaredError = 0;
        int total = 0;

        // Calculate squared error for each prediction
        for (int i = 0; i < predicted.size(); i++) {
            for (int j = 0; j < predicted.get(i).size(); j++) {
                sumSquaredError += Math.pow(predicted.get(i).get(j) - actual.get(i).get(j), 2);
                total++; // Track total number of samples
            }
        }

        return sumSquaredError / total;
    }
}

//Another simple utility class to calculate metrics

//Equinox Electronic