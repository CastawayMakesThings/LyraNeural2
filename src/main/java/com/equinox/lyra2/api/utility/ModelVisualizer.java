package com.equinox.lyra2.api.utility;

import com.equinox.lyra2.objects.Layer;
import com.equinox.lyra2.objects.LyraModel;

public class ModelVisualizer {
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