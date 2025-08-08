package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

/**
 * Serializes neural network models into a compact string format.
 * This class implements a custom serialization format optimized for neural network models,
 * using specific delimiters to separate different components of the model.
 *
 * File Format Structure:
 * 1. Header: [lyraFileHeader][fileVersion]␞
 * 2. Metadata: [modelID]␞[modelAuthor]␞[metadata]␞
 * 3. Version: [lyraVersion]␞
 * 4. I/O Configuration: [inputType]␞[outputType]␞
 * 5. Network Configuration: [activationFunction]␞
 * 6. Front Layer: [neuronCount]␞
 * 7. Hidden Layers: [layer1]/[layer2]/...
 *
 * Delimiter Characters:
 * - ␞ (U+241E): Separates major sections
 * - / : Separates layers
 * - ; : Separates neurons within a layer
 * - ^ : Separates bias from weights in a neuron
 * - , : Separates weights within a neuron
 */
public class Serializer {

    /**
     * Serializes a LyraModel into its string representation.
     * The serialization process follows a specific format that preserves
     * all necessary information to reconstruct the model:
     *
     * 1. Model metadata and configuration
     * 2. Network architecture
     * 3. Layer configurations
     * 4. Neuron parameters (biases and weights)
     *
     * Example format:
     * LyraNeural2.0␞modelID␞author␞metadata␞version␞INPUT␞OUTPUT␞RELU␞32␞
     * 0.5^0.1,0.2,0.3;0.6^0.4,0.5,0.6/0.7^0.8,0.9,1.0
     *
     * @param model The neural network model to serialize
     * @return String representation of the model
     * @throws IllegalArgumentException if the model contains invalid characters in metadata fields
     * @see Parser#parseModelFile(String) for the corresponding deserialization method
     */
    public static String serializeModel(LyraModel model) {
        StringBuilder s = new StringBuilder();

        // Header section
        s.append(Config.lyraFileHeader)
                .append(Config.fileVersion)
                .append("␞");

        // Metadata section
        s.append(model.modelID).append("␞")
                .append(model.modelAuthor).append("␞")
                .append(model.metadata).append("␞");

        // Version
        s.append(model.lyraVersion).append("␞");

        // I/O configuration
        s.append(model.frontLayer.inputType.name()).append("␞")
                .append(model.outputType.name()).append("␞");

        // Network configuration
        s.append(model.activationFunction.name()).append("␞");

        // Front layer size
        s.append(model.frontLayer.neurons.size()).append("␞");

        // Hidden layers serialization
        for (int i = 0; i < model.layers.size(); i++) {
            // Serialize neurons in current layer
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                // Add bias
                s.append(model.layers.get(i).neurons.get(j).bias)
                        .append("^");

                // Add weights
                for (int k = 0; k < model.layers.get(i).neurons.get(j).weights.size(); k++) {
                    s.append(model.layers.get(i).neurons.get(j).weights.get(k));
                    if (k < model.layers.get(i).neurons.get(j).weights.size() - 1) {
                        s.append(",");
                    }
                }
                s.append(";");
            }

            // Add layer separator if not last layer
            if (i < model.layers.size() - 1) {
                s.append("/");
            }
        }

        return s.toString();
    }
}


//This is a simple serializer that takes in a model and spits it out as a parseable string that will be stored
//in a lyrafile.

//Equinox Electronic