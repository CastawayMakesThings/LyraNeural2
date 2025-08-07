package io.github.equinoxelectronic.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DataSplitter {
    public static class SplitData {
        // Contains the training portion of the dataset
        public final ArrayList<ArrayList<Double>> trainData;
        // Contains the testing portion of the dataset
        public final ArrayList<ArrayList<Double>> testData;

        // Constructor to initialize split datasets
        public SplitData(ArrayList<ArrayList<Double>> trainData,
                         ArrayList<ArrayList<Double>> testData) {
            this.trainData = trainData;
            this.testData = testData;
        }
    }

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