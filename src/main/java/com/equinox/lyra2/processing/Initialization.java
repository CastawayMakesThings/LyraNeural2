package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.Enums;
import com.equinox.lyra2.pojo.FrontLayer;
import com.equinox.lyra2.pojo.Layer;
import com.equinox.lyra2.pojo.LyraModel;
import com.equinox.lyra2.pojo.Neuron;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.equinox.lyra2.Config.VERBOSE;

public class Initialization {

    //Takes in an uninitialized model, and returns a trainable initialized model.
    public static LyraModel initializeModel(LyraModel model,
                                            String modelID,
                                            String modelAuthor,
                                            Enums.IOType inputType,
                                            Enums.IOType outputType,
                                            ArrayList<Integer> neuronsPerLayer,
                                            ArrayList<Enums.activationFunctions> activationFunctions) {

        Essentials.logger.logString("Initializing model...");

        //Sets model name
        model.modelID = modelID;

        //Sets model author
        model.modelAuthor = modelAuthor;

        //Sets output type
        model.outputType = outputType;

        //Checks to make sure that the amount of layers inputted matches the amount of activation functions
        if(!(neuronsPerLayer.size() == (activationFunctions.size() - 1) || neuronsPerLayer.size() == activationFunctions.size() )) {
            throw new IllegalArgumentException("The amount of layers inputted does not match the amount of activation functions. ("+activationFunctions.size()+" != "+(neuronsPerLayer.size() + 1)+")\n\n");
        }

        //If the user for some reason sets the first layer's activation function, delete it
        if(activationFunctions.getFirst() == null) {
            activationFunctions.removeFirst();
        }

        //Creates the first layer
        model.frontLayer = new FrontLayer(neuronsPerLayer.getFirst(), inputType);

        //Deletes the first layer's neuron count
        neuronsPerLayer.removeFirst();

        //Creates the other layers
        model.layers = new ArrayList<>();
        for (int i = 0; i < neuronsPerLayer.size(); i++) {
            Layer layer = new Layer(neuronsPerLayer.get(i), activationFunctions.get(i));
            model.layers.add(layer);
        }

        // Adds the layer's neuron's weights and biases
        Random rand = new Random();
        Essentials.logger.logVerbose("Initializing weights and biases...", VERBOSE);

        for (int i = 0; i < model.layers.size(); i++) {
            Layer currentLayer = model.layers.get(i);

            for (int j = 0; j < currentLayer.neurons.size(); j++) {
                Neuron neuron = currentLayer.neurons.get(j);

                // Bias init: small random value
                neuron.bias = rand.nextDouble() - 0.5; // [-0.5, 0.5]

                if (i > 0) {
                    int fanIn = model.layers.get(i - 1).neurons.size();
                    neuron.weights = new ArrayList<>();

                    // Determine standard deviation based on activation
                    double stdDev;
                    if (Objects.requireNonNull(currentLayer.activationFunction) == Enums.activationFunctions.RELU || currentLayer.activationFunction == Enums.activationFunctions.LEAKY_RELU) {
                        stdDev = Math.sqrt(2.0 / fanIn); // He init
                    } else {
                        stdDev = Math.sqrt(1.0 / fanIn); // Xavier init
                    }

                    // Initialize weights using Gaussian with mean=0, std=stdDev
                    for (int k = 0; k < fanIn; k++) {
                        double weight = rand.nextGaussian() * stdDev;
                        neuron.weights.add(weight);
                    }
                } else {
                    int fanIn = model.frontLayer.neurons.size();
                    neuron.weights = new ArrayList<>();

                    // Determine standard deviation based on activation
                    double stdDev;
                    if (Objects.requireNonNull(currentLayer.activationFunction) == Enums.activationFunctions.RELU || currentLayer.activationFunction == Enums.activationFunctions.LEAKY_RELU) {
                        stdDev = Math.sqrt(2.0 / fanIn); // He init
                    } else {
                        stdDev = Math.sqrt(1.0 / fanIn); // Xavier init
                    }

                    // Initialize weights using Gaussian with mean=0, std=stdDev
                    for (int k = 0; k < fanIn; k++) {
                        double weight = rand.nextGaussian() * stdDev;
                        neuron.weights.add(weight);
                    }

                }
            }
        }

        ModelChecker.checkModel(model);

        Essentials.logger.logString("Model initialized.");
        return model;
    }
}
