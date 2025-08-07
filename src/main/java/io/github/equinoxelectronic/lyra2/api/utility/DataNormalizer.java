package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;

public class DataNormalizer {
    public static ArrayList<Double> minMaxNormalize(ArrayList<Double> data) {
        // Find minimum and maximum values in the data
        double min = Collections.min(data);
        double max = Collections.max(data);
        ArrayList<Double> normalized = new ArrayList<>();

        // Scale each value between 0 and 1
        for (Double value : data) {
            normalized.add((value - min) / (max - min));
        }
        return normalized;
    }

    public static ArrayList<Double> zScoreNormalize(ArrayList<Double> data) {
        // Calculate mean and standard deviation
        double mean = calculateMean(data);
        double std = calculateStd(data, mean);
        ArrayList<Double> normalized = new ArrayList<>();

        // Standardize values using z-score formula
        for (Double value : data) {
            normalized.add((value - mean) / std);
        }
        return normalized;
    }

    private static double calculateMean(ArrayList<Double> data) {
        // Calculate average of values using Java streams
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static double calculateStd(ArrayList<Double> data, double mean) {
        // Calculate sum of squared differences from mean
        double sum = data.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .sum();
        // Return square root of average squared difference
        return Math.sqrt(sum / data.size());
    }
}

//A little utility class to help easily normalize data.

//Equinox Electronic
