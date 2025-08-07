package io.github.equinoxelectronic.lyra2.objects;

import java.util.ArrayList;

public class DataSet {
    public ArrayList<ArrayList<Double>> inputs;
    public ArrayList<ArrayList<Double>> outputs;

    public DataSet(ArrayList<ArrayList<Double>> inputs, ArrayList<ArrayList<Double>> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }
}

//This is a general dataset class that stores the inputs and wanted outputs for training.

//Equinox Electronic