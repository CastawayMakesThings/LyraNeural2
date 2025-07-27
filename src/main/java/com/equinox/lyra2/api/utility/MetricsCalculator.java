package com.equinox.lyra2.api.utility;

import java.util.ArrayList;

public class MetricsCalculator {
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