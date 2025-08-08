package io.github.equinoxelectronic.lyra2.objects;

import java.util.ArrayList;

/**
 * Represents a single neuron in a neural network.
 * This class is designed to be lightweight and efficient as neural networks
 * may contain thousands of neuron instances.
 * Each neuron maintains its current activation value, bias term,
 * and connection weights to neurons in the previous layer.
 */
public class Neuron {
    /**
     * The current activation value of this neuron.
     * This value is updated during forward propagation based on inputs
     * from the previous layer, weights, bias, and the activation function.
     */
    public double value;

    /**
     * The bias term for this neuron.
     * Acts as an additional trainable parameter that allows the neuron
     * to learn an offset from zero.
     */
    public double bias;

    /**
     * Connection weights to neurons in the previous layer.
     * The size of this list matches the number of neurons in the previous layer.
     * Each weight corresponds to a connection from a neuron in the previous layer.
     */
    public ArrayList<Double> weights;

    /**
     * Creates a new neuron with default initialization.
     * Sets the initial value and bias to 0 and creates an empty weights list.
     * Weights are typically initialized later based on the network architecture.
     */
    public Neuron() {
        this.value = 0;
        this.bias = 0;
        this.weights = new ArrayList<>();
    }
}


//This is the neuron class. It must be kept simple since potentially thousands of instances or more of this class
//will be in larger models.


//Equinox Electronic