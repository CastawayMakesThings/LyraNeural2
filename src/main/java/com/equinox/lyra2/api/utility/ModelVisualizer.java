package com.equinox.lyra2.api.utility;

import com.equinox.lyra2.objects.Layer;
import com.equinox.lyra2.objects.LyraModel;

public class ModelVisualizer {
    public static String getModelSummary(LyraModel model) {
        StringBuilder summary = new StringBuilder();
        summary.append("Model Structure:\n");
        summary.append("==============\n");

        summary.append("Input Layer: ").append(model.frontLayer.neurons.size())
                .append(" neurons\n");

        for (int i = 0; i < model.layers.size(); i++) {
            Layer layer = model.layers.get(i);
            summary.append("Layer ").append(i + 1).append(": ")
                    .append(layer.neurons.size()).append(" neurons, ")
                    .append("Activation: ").append(layer.activationFunction)
                    .append("\n");
        }

        summary.append("\nTotal Parameters: ").append(calculateParameters(model));

        return summary.toString();
    }

    private static int calculateParameters(LyraModel model) {
        int params = 0;
        int prevSize = model.frontLayer.neurons.size();

        for (Layer layer : model.layers) {
            int neurons = layer.neurons.size();
            // weights + biases
            params += (prevSize * neurons) + neurons;
            prevSize = neurons;
        }

        return params;
    }
}

