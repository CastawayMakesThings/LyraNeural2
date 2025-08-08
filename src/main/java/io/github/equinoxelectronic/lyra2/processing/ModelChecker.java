package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.exceptions.InvalidModelError;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.objects.Neuron;
import io.github.equinoxelectronic.equinox_essentials.Essentials;

import static io.github.equinoxelectronic.lyra2.Config.VERBOSE;

/**
 * Validates neural network model structure and configuration.
 * Performs comprehensive checks on model components to ensure proper initialization
 * and configuration before training or inference.
 */
public class ModelChecker {

    /**
     * Performs comprehensive validation of a neural network model.
     * Checks include:
     * <ul>
     *   <li>Model existence and basic structure</li>
     *   <li>Layer configuration and connectivity</li>
     *   <li>Neuron initialization and properties</li>
     *   <li>Weight and bias validation</li>
     *   <li>Layer connectivity consistency</li>
     * </ul>
     *
     * @param model The neural network model to validate
     * @throws InvalidModelError if any of the following conditions are met:
     *         <ul>
     *           <li>Model or its essential components are null</li>
     *           <li>Model has insufficient number of layers (minimum 3)</li>
     *           <li>Any layer is null or empty</li>
     *           <li>Any layer lacks an activation function</li>
     *           <li>Any neuron is null or improperly configured</li>
     *           <li>Weight matrices have incorrect dimensions</li>
     *           <li>Any weight or bias is NaN</li>
     *         </ul>
     */
    public static void checkModel(LyraModel model) {
        Essentials.logger.logVerbose("Checking entire model...", VERBOSE);

        validateModelBasics(model);
        validateLayers(model);

        Essentials.logger.logVerbose("Model has passed all checks!", VERBOSE);
    }

    /**
     * Validates input type compatibility with the model.
     * Ensures that the provided input matches the model's expected input type.
     *
     * @param model The neural network model
     * @param input The input object to validate
     * @throws InvalidModelError if:
     *         <ul>
     *           <li>Model or input is null</li>
     *           <li>Model's input layer configuration is invalid</li>
     *           <li>Input type doesn't match model's expected type</li>
     *         </ul>
     */
    public static void checkInputType(LyraModel model, Object input) {
        Essentials.logger.logVerbose("Checking input...", VERBOSE);
        validateInputBasics(model, input);

        switch (model.frontLayer.inputType) {
            case RAW -> {} // Raw type accepts any input
            case INTEGER -> validateInputClass(input, Integer.class);
            case FLOAT -> validateInputClass(input, Float.class);
            case DOUBLE -> validateInputClass(input, Double.class);
            case LONG -> validateInputClass(input, Long.class);
            case CHAR -> validateInputClass(input, Character.class);
            default -> throw new InvalidModelError("Unknown input type");
        }
    }

    private static void validateModelBasics(LyraModel model) {
        if (model == null) {
            throw new InvalidModelError("Model is null");
        }
        if (model.frontLayer == null || model.layers == null || model.modelID == null) {
            throw new InvalidModelError("One or more model fields are null");
        }
        if (!(model.layers.size() > 2)) {
            throw new InvalidModelError("Model does not have enough layers to be fed");
        }
    }

    private static void validateLayers(LyraModel model) {
        for (int i = 0; i < model.layers.size(); i++) {
            validateLayer(model, i);
            validateLayerNeurons(model, i);
        }
    }

    private static void validateLayer(LyraModel model, int layerIndex) {
        if (model.layers.get(layerIndex) == null) {
            throw new InvalidModelError("Layer " + layerIndex + " is null!");
        }
        if (model.layers.get(layerIndex).neurons.isEmpty()) {
            throw new InvalidModelError("Layer " + layerIndex + " has no neurons!");
        }
        if (model.layers.get(layerIndex).activationFunction == null) {
            throw new InvalidModelError("Layer " + layerIndex + " has no activation function!");
        }
    }

    private static void validateLayerNeurons(LyraModel model, int layerIndex) {
        Essentials.logger.logVerbose("Checking each neuron in layer " + layerIndex + "...", VERBOSE);

        for (int j = 0; j < model.layers.get(layerIndex).neurons.size(); j++) {
            Neuron neuron = model.layers.get(layerIndex).neurons.get(j);
            validateNeuron(model, layerIndex, j, neuron);
            validateNeuronWeights(model, layerIndex, j, neuron);
        }
    }

    private static void validateNeuron(LyraModel model, int layerIndex, int neuronIndex, Neuron neuron) {
        if (neuron == null) {
            throw new InvalidModelError("Neuron " + neuronIndex + " in layer " + layerIndex + " is null!");
        }
        if (neuron.weights == null) {
            throw new InvalidModelError("Neuron " + neuronIndex + " in layer " + layerIndex + "'s weights are null!");
        }
        if (Double.isNaN(neuron.bias)) {
            throw new InvalidModelError("Neuron " + neuronIndex + " in layer " + layerIndex + "'s bias is NaN!");
        }
    }

    private static void validateNeuronWeights(LyraModel model, int layerIndex, int neuronIndex, Neuron neuron) {
        int expectedWeightCount = layerIndex == 0 ?
                model.frontLayer.neurons.size() :
                model.layers.get(layerIndex - 1).neurons.size();

        if (neuron.weights.size() != expectedWeightCount) {
            throw new InvalidModelError(String.format(
                    "Neuron %d in layer %d's weight count does not match the amount of neurons in the %s layer! (%d != %d)",
                    neuronIndex, layerIndex,
                    layerIndex == 0 ? "front" : "previous",
                    neuron.weights.size(), expectedWeightCount
            ));
        }

        validateWeightValues(model, layerIndex, neuronIndex, neuron);
    }

    private static void validateWeightValues(LyraModel model, int layerIndex, int neuronIndex, Neuron neuron) {
        Essentials.logger.logVerbose("Checking each weight in neuron " + neuronIndex + " in layer " + layerIndex + "...", VERBOSE);

        for (int k = 0; k < neuron.weights.size(); k++) {
            if (Double.isNaN(neuron.weights.get(k))) {
                throw new InvalidModelError("Weight " + k + " in neuron " + neuronIndex + " in layer " + layerIndex + " is NaN!");
            }
        }
    }

    private static void validateInputBasics(LyraModel model, Object input) {
        if (model == null) {
            throw new InvalidModelError("Model is null");
        }
        if (input == null) {
            throw new InvalidModelError("Input is null");
        }
        if (model.frontLayer == null || model.frontLayer.inputType == null) {
            throw new InvalidModelError("Model front layer or input type is null");
        }
    }

    private static void validateInputClass(Object input, Class<?> expectedClass) {
        if (!expectedClass.isInstance(input)) {
            throw new InvalidModelError("Input must be a " + expectedClass.getSimpleName());
        }
    }
}


//This is a relatively simple model checker. It just checks to make sure all the
//required values of a model exist, and that there are no obvious issues in the
//data.

//Equinox Electronic