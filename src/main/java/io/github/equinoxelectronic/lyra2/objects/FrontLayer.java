package io.github.equinoxelectronic.lyra2.objects;

import io.github.equinoxelectronic.lyra2.Enums;

import java.util.ArrayList;

/**
 * Represents the input layer of a neural network model.
 * Unlike other layers, the front layer serves primarily as an input buffer
 * and does not participate in weight updates during training.
 * It handles the initial data input and type validation for the network.
 */
public class FrontLayer {
    /**
     * The collection of neurons in the input layer.
     * These neurons only store input values and do not have weights or biases
     * as they serve as the network's input buffer.
     */
    public ArrayList<Neuron> neurons;

    /**
     * Specifies the expected data type for model inputs.
     * Used for input validation and data conversion before processing.
     */
    public Enums.IOType inputType;

    /**
     * Creates a new front layer with the specified number of neurons and input type.
     * Initializes neurons without weights or biases as they are not needed for the input layer.
     *
     * @param neuronsCount The number of input neurons (input dimensions)
     * @param inputType The expected type of input data (from IOType enum)
     */
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
