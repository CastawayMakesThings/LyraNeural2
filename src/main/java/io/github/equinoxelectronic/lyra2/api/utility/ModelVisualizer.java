package io.github.equinoxelectronic.lyra2.api.utility;

import io.github.equinoxelectronic.lyra2.objects.Layer;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

/**
 * Utility class for visualizing neural network model architectures.
 * Provides methods to generate human-readable representations of model structure
 * and calculate model complexity metrics.
 */
public class ModelVisualizer {

    /**
     * Generates a text-based summary of the neural network model's architecture.
     * The summary includes:
     * - Input layer size
     * - Hidden layer sizes
     * - Total number of trainable parameters
     *
     * Example output:
     * Model Structure:
     * ==============
     * Input Layer: 10 neurons
     * Layer 1: 32 neurons
     * Layer 2: 16 neurons
     *
     * Total Parameters: 842
     *
     * @param model The LyraModel instance to be visualized
     * @return A formatted string containing the model's structural summary
     */
    public static String getModelSummary(LyraModel model) {
        StringBuilder summary = new StringBuilder();
        // Add header sections
        summary.append("Model Structure:\n");
        summary.append("==============\n");

        // Add input layer info
        summary.append("Input Layer: ").append(model.frontLayer.neurons.size())
                .append(" neurons\n");

        // Loop through and add info for each hidden/output layer
        for (int i = 0; i < model.layers.size(); i++) {
            Layer layer = model.layers.get(i);
            summary.append("Layer ").append(i + 1).append(": ")
                    .append(layer.neurons.size()).append(" neurons")
                    .append("\n");
        }

        // Add total parameter count
        summary.append("\nTotal Parameters: ").append(calculateParameters(model));

        return summary.toString();
    }

    /**
     * Calculates the total number of trainable parameters in the model.
     * For each layer, the number of parameters is calculated as:
     * (number of inputs * number of neurons) + number of neurons
     * where the additional term accounts for the bias parameter per neuron.
     *
     * @param model The LyraModel instance for which to calculate parameters
     * @return The total number of trainable parameters in the model
     */
    private static int calculateParameters(LyraModel model) {
        int params = 0;
        int prevSize = model.frontLayer.neurons.size();

        for (Layer layer : model.layers) {
            int neurons = layer.neurons.size();
            // For each neuron: weights from previous layer + one bias
            params += (prevSize * neurons) + neurons;
            prevSize = neurons;
        }

        return params;
    }
}




//Here are some tools that visualize a model in the terminal. Although it is not very useful,
//it still is cool.

//Equinox Electronic