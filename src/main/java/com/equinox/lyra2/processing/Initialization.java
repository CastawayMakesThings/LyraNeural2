package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.Enums;
import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.FrontLayer;
import com.equinox.lyra2.objects.Layer;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.objects.Neuron;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.equinox.lyra2.Config.VERBOSE;
import static com.equinox.lyra2.Config.version;

public class Initialization {

    //Takes in an uninitialized model, and returns a trainable initialized model.
    public static LyraModel initializeModel(LyraModel model,
                                            String modelID,
                                            String modelAuthor,
                                            Enums.IOType inputType,
                                            Enums.IOType outputType,
                                            ArrayList<Integer> neuronsPerLayer,
                                            ArrayList<Enums.activationFunctions> activationFunctions,
                                            int firstLayerSize,
                                            int lastLayerSize,
                                            Enums.activationFunctions lastLayerActivationFunction) {

        Essentials.logger.logString("Initializing model...");

        //Sets model name
        model.modelID = modelID;

        //Sets model author
        model.modelAuthor = modelAuthor;

        //Sets model version
        model.lyraVersion = version;

        //Sets model metadata to empty
        model.metadata = "";

        //Sets output type
        model.outputType = outputType;

        //Checks to make sure that the amount of layers inputted matches the amount of activation functions
        if(!(neuronsPerLayer.size() == (activationFunctions.size() - 1) || neuronsPerLayer.size() == activationFunctions.size() )) {
            throw new IllegalArgumentException("The amount of layers inputted does not match the amount of activation functions. ("+activationFunctions.size()+" != "+(neuronsPerLayer.size() + 1)+")\n\n");
        }

        //Creates the first layer
        if(inputType != Enums.IOType.RAW) {
            try {
                model.frontLayer = new FrontLayer(DatatypeConversion.getBitCount(inputType), inputType);
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        } else {
            model.frontLayer = new FrontLayer(firstLayerSize, inputType);
        }

        //Deletes the first layer's neuron count
        neuronsPerLayer.removeFirst();

        //Creates the other layers
        model.layers = new ArrayList<>();
        for (int i = 0; i < neuronsPerLayer.size(); i++) {
            Layer layer = new Layer(neuronsPerLayer.get(i), activationFunctions.get(i));
            model.layers.add(layer);
        }

        //Creates the last layer
        if(outputType != Enums.IOType.RAW) {
            try {
                model.layers.add(new Layer(DatatypeConversion.getBitCount(outputType), lastLayerActivationFunction));
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        } else {
            model.layers.add(new Layer(lastLayerSize, lastLayerActivationFunction));
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
