package com.equinox.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;

public class DataNormalizer {
    public static ArrayList<Double> minMaxNormalize(ArrayList<Double> data) {
        double min = Collections.min(data);
        double max = Collections.max(data);
        ArrayList<Double> normalized = new ArrayList<>();

        for (Double value : data) {
            normalized.add((value - min) / (max - min));
        }
        return normalized;
    }

    public static ArrayList<Double> zScoreNormalize(ArrayList<Double> data) {
        double mean = calculateMean(data);
        double std = calculateStd(data, mean);
        ArrayList<Double> normalized = new ArrayList<>();

        for (Double value : data) {
            normalized.add((value - mean) / std);
        }
        return normalized;
    }

    private static double calculateMean(ArrayList<Double> data) {
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static double calculateStd(ArrayList<Double> data, double mean) {
        double sum = data.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .sum();
        return Math.sqrt(sum / data.size());
    }
}
