package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility class providing data normalization methods for numerical data preprocessing.
 * This class includes implementations of common normalization techniques used in
 * machine learning and data analysis, such as Min-Max scaling and Z-Score normalization.
 */
public class DataNormalizer {

    /**
     * Performs Min-Max normalization on a list of numerical data.
     * Scales all values to a range between 0 and 1 using the formula:
     * x_normalized = (x - min) / (max - min)
     * where x is the original value, min is the minimum value in the dataset,
     * and max is the maximum value in the dataset.
     *
     * @param data The ArrayList of Double values to be normalized
     * @return A new ArrayList containing the normalized values between 0 and 1
     */
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

    /**
     * Performs Z-Score normalization (standardization) on a list of numerical data.
     * Transforms the data to have a mean of 0 and a standard deviation of 1 using the formula:
     * z = (x - μ) / σ
     * where x is the original value, μ is the mean of the data,
     * and σ is the standard deviation of the data.
     *
     * @param data The ArrayList of Double values to be normalized
     * @return A new ArrayList containing the standardized values
     */
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

    /**
     * Calculates the arithmetic mean of a list of numbers.
     *
     * @param data The ArrayList of Double values
     * @return The arithmetic mean of the input data
     */
    private static double calculateMean(ArrayList<Double> data) {
        // Calculate average of values using Java streams
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Calculates the standard deviation of a list of numbers.
     * Uses the formula: σ = sqrt(Σ(x - μ)² / N)
     * where x is each value, μ is the mean, and N is the number of values.
     *
     * @param data The ArrayList of Double values
     * @param mean The pre-calculated mean of the data
     * @return The standard deviation of the input data
     */
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
