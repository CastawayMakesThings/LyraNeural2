package com.equinox.lyra2.objects;

import java.util.ArrayList;

public class Neuron {
    public double value;
    public double bias;
    public ArrayList<Double> weights;

    public Neuron() {
        this.value = 0;
        this.bias = 0;
        this.weights = new ArrayList<>();
    }
}
