package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraModelLoadingError;
import io.github.equinoxelectronic.lyra2.objects.FrontLayer;
import io.github.equinoxelectronic.lyra2.objects.Layer;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.objects.Neuron;

import java.util.ArrayList;

/**
 * Parser for Lyra model files. Converts the string representation of a model
 * into a fully instantiated LyraModel object.
 *
 * File Format Specification:
 * - Fields are separated by the '␞' character
 * - Layers are separated by '/'
 * - Neurons within a layer are separated by ';'
 * - Neuron bias and weights are separated by '^'
 * - Weights are separated by ','
 */
public class Parser {

    /**
     * Parses a string representation of a Lyra model file and constructs a LyraModel object.
     * The parsing process includes:
     * 1. Validation of file header and version
     * 2. Extraction of model metadata (ID, author, version)
     * 3. Configuration of model parameters (I/O types, activation function)
     * 4. Construction of network architecture (layers and neurons)
     * 5. Population of neuron weights and biases
     *
     * File Structure:
     * [0] - Header and version
     * [1] - Model ID
     * [2] - Model author
     * [3] - Metadata
     * [4] - Lyra version
     * [5] - Input type
     * [6] - Output type
     * [7] - Activation function
     * [8] - Front layer size
     * [9] - Network layers data
     *
     * @param fileContent String containing the model data in Lyra format
     * @return Fully constructed and configured LyraModel instance
     * @throws LyraModelLoadingError if the file format is invalid or version is incompatible
     */
    public static LyraModel parseModelFile(String fileContent) throws LyraModelLoadingError {
        String[] parts = fileContent.split("␞");

        // Verify header and version
        if (!parts[0].startsWith(Config.lyraFileHeader + Config.fileVersion)) {
            throw new LyraModelLoadingError("Invalid file format or version mismatch");
        }

        // Initialize model and set metadata
        LyraModel model = new LyraModel();
        model.modelID = parts[1];
        model.modelAuthor = parts[2];
        model.metadata = parts[3];
        model.lyraVersion = parts[4];

        // Configure I/O types
        Enums.IOType inputType = Enums.IOType.valueOf(parts[5]);
        model.outputType = Enums.IOType.valueOf(parts[6]);

        // Set activation function
        model.activationFunction = Enums.activationFunctions.valueOf(parts[7]);

        // Initialize input layer
        int frontLayerSize = Integer.parseInt(parts[8]);
        model.frontLayer = new FrontLayer(frontLayerSize, inputType);

        // Parse and construct network layers
        String[] layersData = parts[9].split("/");
        model.layers = new ArrayList<>();

        for (String layerData : layersData) {
            if (layerData.isEmpty()) continue;

            // Parse neurons in current layer
            String[] neuronsData = layerData.split(";");
            Layer layer = new Layer(neuronsData.length, model.activationFunction);

            // Configure each neuron
            for (int i = 0; i < neuronsData.length; i++) {
                if (neuronsData[i].isEmpty()) continue;

                // Parse bias and weights
                String[] neuronParts = neuronsData[i].split("\\^");
                Neuron neuron = layer.neurons.get(i);

                // Set bias value
                neuron.bias = Double.parseDouble(neuronParts[0]);

                // Parse and set weights
                String[] weightStrings = neuronParts[1].split(",");
                neuron.weights = new ArrayList<>();
                for (String w : weightStrings) {
                    if (!w.isEmpty()) {
                        neuron.weights.add(Double.parseDouble(w));
                    }
                }
            }

            model.layers.add(layer);
        }

        return model;
    }
}



//This is a simple parser that takes in a string version of a model and spits out a complete model.

//Equinox Electronic
