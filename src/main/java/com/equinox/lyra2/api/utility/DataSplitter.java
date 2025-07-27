package com.equinox.lyra2.api.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DataSplitter {
    public static class SplitData {
        public final ArrayList<ArrayList<Double>> trainData;
        public final ArrayList<ArrayList<Double>> testData;

        public SplitData(ArrayList<ArrayList<Double>> trainData,
                         ArrayList<ArrayList<Double>> testData) {
            this.trainData = trainData;
            this.testData = testData;
        }
    }

    public static SplitData trainTestSplit(ArrayList<ArrayList<Double>> data,
                                           double testSize,
                                           long seed) {
        Random random = new Random(seed);
        ArrayList<ArrayList<Double>> shuffledData = new ArrayList<>(data);
        Collections.shuffle(shuffledData, random);

        int testSize_ = (int) (data.size() * testSize);
        ArrayList<ArrayList<Double>> trainData = new ArrayList<>(
                shuffledData.subList(testSize_, shuffledData.size()));
        ArrayList<ArrayList<Double>> testData = new ArrayList<>(
                shuffledData.subList(0, testSize_));

        return new SplitData(trainData, testData);
    }
}
