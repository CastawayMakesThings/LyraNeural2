package io.github.equinoxelectronic.lyra2.objects;

import io.github.equinoxelectronic.lyra2.Enums;

import java.util.ArrayList;

public class Layer {
    //The neurons in the layer
    public ArrayList<Neuron> neurons;
    //The activation function for this layer.
    public Enums.activationFunctions activationFunction;

    //A very minimal constructor
    public Layer(int neuronsCount, Enums.activationFunctions activationFunction) {
         this.neurons = new ArrayList<>();
         for (int i = 0; i < neuronsCount; i++) {
             this.neurons.add(new Neuron());
         }
         this.activationFunction = activationFunction;
    }
}

//This is a single layer of a model. All layers, save the first one, will
//be an instance of this. Also note that to have each layer have its own activation function
//is deprecated, so I intend to take out the activationFunction value eventually.

//Equinox Electronic