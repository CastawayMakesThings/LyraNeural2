package io.github.equinoxelectronic.lyra2.objects;

import io.github.equinoxelectronic.lyra2.Enums;

import java.util.ArrayList;

/**
 * Represents a layer in a neural network, containing neurons and their activation function.
 * This class is used for all layers except the input layer (FrontLayer).
 * Each layer contains a collection of neurons and applies a specified activation function
 * to their outputs during forward propagation.
 *
 * @deprecated The per-layer activation function is being deprecated in favor of a
 *             model-wide activation function. The activationFunction field will be
 *             removed in a future version.
 */
public class Layer {
    /**
     * Collection of neurons that make up this layer.
     * Each neuron contains its own weights and bias values.
     */
    public ArrayList<Neuron> neurons;

    /**
     * The activation function applied to all neurons in this layer.
     *
     * @deprecated This field will be removed in favor of a model-wide activation function.
     *             Storing activation functions per layer is no longer recommended.
     */
    @Deprecated
    public Enums.activationFunctions activationFunction;

    /**
     * Creates a new layer with the specified number of neurons and activation function.
     * Initializes each neuron with default values (weights and biases will be set later).
     *
     * @param neuronsCount The number of neurons to create in this layer
     * @param activationFunction The activation function to use for this layer
     * @deprecated The activation function parameter will be removed in a future version.
     *             Use the constructor without activation function when it becomes available.
     */
    @Deprecated
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