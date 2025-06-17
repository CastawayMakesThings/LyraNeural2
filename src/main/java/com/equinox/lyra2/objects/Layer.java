package com.equinox.lyra2.objects;

import com.equinox.lyra2.Enums;

import java.util.ArrayList;

public class Layer {
    public ArrayList<Neuron> neurons;
    public Enums.activationFunctions activationFunction;

    public Layer(int neuronsCount, Enums.activationFunctions activationFunction) {
         this.neurons = new ArrayList<>();
         for (int i = 0; i < neuronsCount; i++) {
             this.neurons.add(new Neuron());
         }
         this.activationFunction = activationFunction;
    }
}
