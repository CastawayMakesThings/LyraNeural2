package com.equinox.lyra2.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
