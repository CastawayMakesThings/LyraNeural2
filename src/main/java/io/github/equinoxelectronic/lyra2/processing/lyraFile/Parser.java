package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraModelLoadingError;
import io.github.equinoxelectronic.lyra2.objects.FrontLayer;
import io.github.equinoxelectronic.lyra2.objects.Layer;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.objects.Neuron;

import java.util.ArrayList;

public class Parser {
    public static LyraModel parseModelFile(String fileContent) throws LyraModelLoadingError {
        // Split the content by the file separator character
        String[] parts = fileContent.split("␞");

        // Verify header
        if (!parts[0].startsWith(Config.lyraFileHeader + Config.fileVersion)) {
            throw new LyraModelLoadingError("Invalid file format or version mismatch");
        }

        // Create new model and set basic properties
        LyraModel model = new LyraModel();
        model.modelID = parts[1];
        model.modelAuthor = parts[2];
        model.metadata = parts[3];
        model.lyraVersion = parts[4];

        // Set input/output types
        Enums.IOType inputType = Enums.IOType.valueOf(parts[5]);
        model.outputType = Enums.IOType.valueOf(parts[6]);

        // Set activation function
        model.activationFunction = Enums.activationFunctions.valueOf(parts[7]);

        // Create front layer
        int frontLayerSize = Integer.parseInt(parts[8]);
        model.frontLayer = new FrontLayer(frontLayerSize, inputType);

        // Process layers
        String[] layersData = parts[9].split("/");
        model.layers = new ArrayList<>();

        // For each layer
        for (String layerData : layersData) {
            if (layerData.isEmpty()) continue;

            // Split into neurons
            String[] neuronsData = layerData.split(";");
            Layer layer = new Layer(neuronsData.length, model.activationFunction);

            // For each neuron in the layer
            for (int i = 0; i < neuronsData.length; i++) {
                if (neuronsData[i].isEmpty()) continue;

                // Split into bias and weights
                String[] neuronParts = neuronsData[i].split("\\^");
                Neuron neuron = layer.neurons.get(i);

                // Set bias
                neuron.bias = Double.parseDouble(neuronParts[0]);

                // Set weights
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
