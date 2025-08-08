package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.equinox_essentials.Essentials;
import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;
import io.github.equinoxelectronic.lyra2.objects.FrontLayer;
import io.github.equinoxelectronic.lyra2.objects.Layer;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.objects.Neuron;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static io.github.equinoxelectronic.lyra2.Config.VERBOSE;
import static io.github.equinoxelectronic.lyra2.Config.version;

/**
 * Handles neural network model initialization with advanced weight initialization strategies.
 * Implements He initialization for ReLU-based networks and Xavier initialization for other activation functions.
 */
public class Initialization {

    /**
     * Initializes a neural network model with the specified architecture and parameters.
     * Implements intelligent weight initialization based on activation functions:
     * - He initialization for ReLU/Leaky ReLU (std = sqrt(2/fan_in))
     * - Xavier initialization for other functions (std = sqrt(1/fan_in))
     *
     * @param model Empty model instance to initialize
     * @param modelID Unique identifier for the model
     * @param modelAuthor Author/creator of the model
     * @param inputType Input data type (RAW or specific type)
     * @param outputType Output data type (RAW or specific type)
     * @param neuronsPerLayer List of neuron counts for each hidden layer
     * @param activationFunctions List of activation functions for each layer
     * @param firstLayerSize Size of input layer (used only for RAW input type)
     * @param lastLayerSize Size of output layer (used only for RAW output type)
     * @param lastLayerActivationFunction Activation function for output layer
     * @param activationFunction Default activation function for hidden layers
     * @return Initialized neural network model ready for training
     * @throws IllegalArgumentException if layer count doesn't match activation function count
     * @throws RuntimeException if data type conversion fails
     */
    public static LyraModel initializeModel(LyraModel model,
                                            String modelID,
                                            String modelAuthor,
                                            Enums.IOType inputType,
                                            Enums.IOType outputType,
                                            ArrayList<Integer> neuronsPerLayer,
                                            ArrayList<Enums.activationFunctions> activationFunctions,
                                            int firstLayerSize,
                                            int lastLayerSize,
                                            Enums.activationFunctions lastLayerActivationFunction,
                                            Enums.activationFunctions activationFunction) {
        Essentials.logger.logString("Initializing model...");

        initializeModelMetadata(model, modelID, modelAuthor, outputType, activationFunction);
        validateLayerConfiguration(neuronsPerLayer, activationFunctions);
        createInputLayer(model, inputType, firstLayerSize);
        createHiddenLayers(model, neuronsPerLayer, activationFunctions);
        createOutputLayer(model, outputType, lastLayerSize, lastLayerActivationFunction);
        initializeWeightsAndBiases(model);

        ModelChecker.checkModel(model);
        Essentials.logger.logString("Model initialized.");
        return model;
    }

    private static void initializeModelMetadata(LyraModel model, String modelID, String modelAuthor,
                                                Enums.IOType outputType, Enums.activationFunctions activationFunction) {
        model.modelID = modelID;
        model.modelAuthor = modelAuthor;
        model.lyraVersion = version;
        model.activationFunction = activationFunction;
        model.metadata = "";
        model.outputType = outputType;
    }

    private static void validateLayerConfiguration(ArrayList<Integer> neuronsPerLayer,
                                                   ArrayList<Enums.activationFunctions> activationFunctions) {
        if (!(neuronsPerLayer.size() == (activationFunctions.size() - 1) ||
                neuronsPerLayer.size() == activationFunctions.size())) {
            throw new IllegalArgumentException(
                    "Layer count doesn't match activation function count: " +
                            activationFunctions.size() + " != " + (neuronsPerLayer.size() + 1));
        }
    }

    private static void createInputLayer(LyraModel model, Enums.IOType inputType, int firstLayerSize) {
        if (inputType != Enums.IOType.RAW) {
            try {
                model.frontLayer = new FrontLayer(DatatypeConversion.getBitCount(inputType), inputType);
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException("Failed to create input layer: " + e.getMessage(), e);
            }
        } else {
            model.frontLayer = new FrontLayer(firstLayerSize, inputType);
        }
    }

    private static void createHiddenLayers(LyraModel model, ArrayList<Integer> neuronsPerLayer,
                                           ArrayList<Enums.activationFunctions> activationFunctions) {
        neuronsPerLayer.removeFirst(); // Remove input layer size
        model.layers = new ArrayList<>();
        for (int i = 0; i < neuronsPerLayer.size(); i++) {
            model.layers.add(new Layer(neuronsPerLayer.get(i), activationFunctions.get(i)));
        }
    }

    private static void createOutputLayer(LyraModel model, Enums.IOType outputType,
                                          int lastLayerSize, Enums.activationFunctions lastLayerActivationFunction) {
        try {
            if (outputType != Enums.IOType.RAW) {
                model.layers.add(new Layer(DatatypeConversion.getBitCount(outputType), lastLayerActivationFunction));
            } else {
                model.layers.add(new Layer(lastLayerSize, lastLayerActivationFunction));
            }
        } catch (LyraWrongDatatypeException e) {
            throw new RuntimeException("Failed to create output layer: " + e.getMessage(), e);
        }
    }

    private static void initializeWeightsAndBiases(LyraModel model) {
        Random rand = new Random();
        Essentials.logger.logVerbose("Initializing weights and biases...", VERBOSE);

        for (int i = 0; i < model.layers.size(); i++) {
            Layer currentLayer = model.layers.get(i);
            int fanIn = (i > 0) ? model.layers.get(i - 1).neurons.size() : model.frontLayer.neurons.size();

            for (Neuron neuron : currentLayer.neurons) {
                initializeNeuron(neuron, fanIn, currentLayer.activationFunction, rand);
            }
        }
    }

    private static void initializeNeuron(Neuron neuron, int fanIn,
                                         Enums.activationFunctions activationFunction, Random rand) {
        neuron.bias = rand.nextDouble() - 0.5; // Initialize bias in [-0.5, 0.5]
        neuron.weights = new ArrayList<>();

        // Choose initialization strategy based on activation function
        double stdDev = (Objects.requireNonNull(activationFunction) == Enums.activationFunctions.RELU ||
                activationFunction == Enums.activationFunctions.LEAKY_RELU) ?
                Math.sqrt(2.0 / fanIn) :  // He initialization
                Math.sqrt(1.0 / fanIn);    // Xavier initialization

        // Initialize weights using Gaussian distribution
        for (int i = 0; i < fanIn; i++) {
            neuron.weights.add(rand.nextGaussian() * stdDev);
        }
    }
}



//This is what sets the initial values for a model. Depending on the activation function,
//it will use He or Xavier initialization, with a Gaussian applied.

//Equinox Electronic