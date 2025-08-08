package io.github.equinoxelectronic.lyra2.objects;

import java.util.ArrayList;

/**
 * Represents a dataset for neural network training, containing paired input and output samples.
 * Each sample consists of a vector of double values, stored as ArrayLists.
 * The inputs and outputs are stored as 2D ArrayLists where:
 * - The outer ArrayList represents different samples
 * - The inner ArrayList represents the features/values for each sample
 */
public class DataSet {
    /**
     * The input samples for training.
     * Each inner ArrayList represents one input vector.
     */
    public ArrayList<ArrayList<Double>> inputs;

    /**
     * The corresponding output (target) samples for training.
     * Each inner ArrayList represents one output vector.
     * The index of each output corresponds to the input at the same index.
     */
    public ArrayList<ArrayList<Double>> outputs;

    /**
     * Creates a new dataset with the specified input and output samples.
     *
     * @param inputs The input samples, where each inner ArrayList is one input vector
     * @param outputs The corresponding output samples, where each inner ArrayList is one output vector.
     *                Must have the same number of samples (outer ArrayList size) as inputs.
     */
    public DataSet(ArrayList<ArrayList<Double>> inputs, ArrayList<ArrayList<Double>> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }
}


//This is a general dataset class that stores the inputs and wanted outputs for training.

//Equinox Electronic