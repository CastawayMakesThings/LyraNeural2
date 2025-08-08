package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;

/**
 * Utility class for validating neural network input and output data.
 * Provides methods to ensure data consistency and validity before model training or inference.
 */
public class ValidationUtils {

    /**
     * Validates the input and output datasets for neural network operations.
     * Performs the following checks:
     * <ul>
     *   <li>Ensures neither inputs nor outputs are null</li>
     *   <li>Verifies that input and output datasets have the same number of samples</li>
     *   <li>Checks that datasets are not empty</li>
     *   <li>Ensures all input samples have consistent dimensions</li>
     *   <li>Ensures all output samples have consistent dimensions</li>
     * </ul>
     *
     * @param inputs The input dataset as a 2D ArrayList where:
     *              - First dimension represents different samples
     *              - Second dimension represents features of each sample
     * @param outputs The output dataset as a 2D ArrayList where:
     *              - First dimension represents different samples
     *              - Second dimension represents target values for each sample
     * @throws IllegalArgumentException if any validation check fails, with a descriptive message of the specific issue:
     *         - If inputs or outputs are null
     *         - If inputs and outputs have different numbers of samples
     *         - If either dataset is empty
     *         - If input samples have inconsistent dimensions
     *         - If output samples have inconsistent dimensions
     */
    public static void validateInputs(ArrayList<ArrayList<Double>> inputs,
                                      ArrayList<ArrayList<Double>> outputs) {
        // Check if either input or output is null
        if (inputs == null || outputs == null) {
            throw new IllegalArgumentException("Input and output data cannot be null");
        }

        // Verify that input and output datasets have same number of samples
        if (inputs.size() != outputs.size()) {
            throw new IllegalArgumentException(
                    "Input and output datasets must have the same number of samples");
        }

        // Check if datasets are empty
        if (inputs.isEmpty() || outputs.isEmpty()) {
            throw new IllegalArgumentException("Input and output datasets cannot be empty");
        }

        // Get dimensions of first input and output samples
        int inputSize = inputs.get(0).size();
        int outputSize = outputs.get(0).size();

        // Verify all input samples have same dimension
        for (ArrayList<Double> input : inputs) {
            if (input.size() != inputSize) {
                throw new IllegalArgumentException(
                        "All input samples must have the same dimension");
            }
        }

        // Verify all output samples have same dimension
        for (ArrayList<Double> output : outputs) {
            if (output.size() != outputSize) {
                throw new IllegalArgumentException(
                        "All output samples must have the same dimension");
            }
        }
    }
}


//A simple validation class. It validates data to make sure it is valid.

//Equinox Electronic