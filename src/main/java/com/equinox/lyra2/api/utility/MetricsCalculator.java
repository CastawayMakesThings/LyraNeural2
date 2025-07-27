package com.equinox.lyra2.api.utility;

import java.util.ArrayList;

public class MetricsCalculator {
    public static double calculateAccuracy(ArrayList<ArrayList<Double>> predicted,
                                           ArrayList<ArrayList<Double>> actual) {
        if (predicted.size() != actual.size()) {
            throw new IllegalArgumentException("Predicted and actual sizes must match");
        }

        int correct = 0;
        int total = 0;

        for (int i = 0; i < predicted.size(); i++) {
            for (int j = 0; j < predicted.get(i).size(); j++) {
                if (Math.abs(predicted.get(i).get(j) - actual.get(i).get(j)) < 0.5) {
                    correct++;
                }
                total++;
            }
        }

        return (double) correct / total;
    }

    public static double calculateMSE(ArrayList<ArrayList<Double>> predicted,
                                      ArrayList<ArrayList<Double>> actual) {
        if (predicted.size() != actual.size()) {
            throw new IllegalArgumentException("Predicted and actual sizes must match");
        }

        double sumSquaredError = 0;
        int total = 0;

        for (int i = 0; i < predicted.size(); i++) {
            for (int j = 0; j < predicted.get(i).size(); j++) {
                sumSquaredError += Math.pow(predicted.get(i).get(j) - actual.get(i).get(j), 2);
                total++;
            }
        }

        return sumSquaredError / total;
    }
}
