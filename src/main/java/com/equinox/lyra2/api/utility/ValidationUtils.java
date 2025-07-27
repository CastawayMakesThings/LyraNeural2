package com.equinox.lyra2.api.utility;

import java.util.ArrayList;

public class ValidationUtils {
    public static void validateInputs(ArrayList<ArrayList<Double>> inputs,
                                      ArrayList<ArrayList<Double>> outputs) {
        if (inputs == null || outputs == null) {
            throw new IllegalArgumentException("Input and output data cannot be null");
        }

        if (inputs.size() != outputs.size()) {
            throw new IllegalArgumentException(
                    "Input and output datasets must have the same number of samples");
        }

        if (inputs.isEmpty() || outputs.isEmpty()) {
            throw new IllegalArgumentException("Input and output datasets cannot be empty");
        }

        int inputSize = inputs.get(0).size();
        int outputSize = outputs.get(0).size();

        for (ArrayList<Double> input : inputs) {
            if (input.size() != inputSize) {
                throw new IllegalArgumentException(
                        "All input samples must have the same dimension");
            }
        }

        for (ArrayList<Double> output : outputs) {
            if (output.size() != outputSize) {
                throw new IllegalArgumentException(
                        "All output samples must have the same dimension");
            }
        }
    }
}

