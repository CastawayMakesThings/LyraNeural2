
package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Utility class for splitting datasets into training and testing sets.
 * Provides functionality to randomly partition data while maintaining reproducibility
 * through seed-based randomization.
 */
public class DataSplitter {

    /**
     * Inner class representing split dataset containing both training and testing portions.
     */
    public static class SplitData {
        /** Contains the training portion of the dataset */
        public final ArrayList<ArrayList<Double>> trainData;
        /** Contains the testing portion of the dataset */
        public final ArrayList<ArrayList<Double>> testData;

        /**
         * Constructor to initialize split datasets.
         *
         * @param trainData The training portion of the split dataset
         * @param testData The testing portion of the split dataset
         */
        public SplitData(ArrayList<ArrayList<Double>> trainData,
                         ArrayList<ArrayList<Double>> testData) {
            this.trainData = trainData;
            this.testData = testData;
        }
    }

    /**
     * Splits a dataset into training and testing sets using random sampling.
     * The method ensures reproducibility by using a seed for random number generation.
     * The data is first shuffled randomly and then split according to the specified ratio.
     *
     * @param data The complete dataset to be split
     * @param testSize The proportion of the dataset to include in the test split (0.0 to 1.0)
     * @param seed Random seed for reproducible splitting
     * @return A SplitData object containing the training and testing datasets
     * @throws IllegalArgumentException if testSize is not between 0 and 1
     */
    public static SplitData trainTestSplit(ArrayList<ArrayList<Double>> data,
                                           double testSize,
                                           long seed) {
        // Initialize random number generator with seed for reproducibility
        Random random = new Random(seed);
        // Create copy of data for shuffling
        ArrayList<ArrayList<Double>> shuffledData = new ArrayList<>(data);
        // Randomly shuffle the data
        Collections.shuffle(shuffledData, random);

        // Calculate size of test set based on testSize ratio
        int testSize_ = (int) (data.size() * testSize);
        // Create training set from remaining data after test set
        ArrayList<ArrayList<Double>> trainData = new ArrayList<>(
                shuffledData.subList(testSize_, shuffledData.size()));
        // Create test set from first portion of shuffled data
        ArrayList<ArrayList<Double>> testData = new ArrayList<>(
                shuffledData.subList(0, testSize_));

        return new SplitData(trainData, testData);
    }
}


//Another tiny utility method to split data

//Equinox Electronic