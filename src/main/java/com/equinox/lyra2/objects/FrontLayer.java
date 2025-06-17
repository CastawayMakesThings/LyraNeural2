package com.equinox.lyra2.objects;

import com.equinox.lyra2.Enums;

import java.util.ArrayList;

public class FrontLayer {
    public ArrayList<Neuron> neurons;
    public Enums.IOType inputType;

    public FrontLayer(int neuronsCount, Enums.IOType inputType) {
        this.neurons = new ArrayList<>();
        for (int i = 0; i < neuronsCount; i++) {
            this.neurons.add(new Neuron());
        }
        this.inputType = inputType;
    }
}
