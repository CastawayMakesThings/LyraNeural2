package com.equinox.lyra2.objects;

import java.util.ArrayList;

public class Neuron {
    //This represents the neuron's current value
    public double value;
    //The neuron's bias
    public double bias;
    //The weights of neuron. Weight Count == Neuron Count in previous layer.
    public ArrayList<Double> weights;

    //Very simple initializer
    public Neuron() {
        this.value = 0;
        this.bias = 0;
        this.weights = new ArrayList<>();
    }
}

//This is the neuron class. It must be kept simple since potentially thousands of instances or more of this class
//will be in larger models.


//Equinox Electronic