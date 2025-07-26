package com.equinox.lyra2.objects;

import com.equinox.lyra2.Enums;

import java.util.ArrayList;

public class FrontLayer {
    //The neurons in the front layer.
    public ArrayList<Neuron> neurons;
    //The input type for the entire model
    public Enums.IOType inputType;

    //A very simple constructor
    public FrontLayer(int neuronsCount, Enums.IOType inputType) {
        this.neurons = new ArrayList<>();
        for (int i = 0; i < neuronsCount; i++) {
            this.neurons.add(new Neuron());
        }
        this.inputType = inputType;
    }
}

//This class is the front/first layer of a model. All this contains are the neurons,
//and the datatype for the model input. Although that could have been stored in the
//LyraModel object, it sits here for now. The reason why the front layer
//gets its own class is because the front layer does not need to be modified
//during training, and it's weights and biases dont matter at all.


//Equinox Electronic
