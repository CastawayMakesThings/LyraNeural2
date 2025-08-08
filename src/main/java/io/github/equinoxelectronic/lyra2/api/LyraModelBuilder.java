package io.github.equinoxelectronic.lyra2.api;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.InvalidModelError;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.Initialization;
import io.github.equinoxelectronic.equinox_essentials.Essentials;

import java.util.ArrayList;

/**
 * Builder class for configuring and creating LyraModel instances.
 * Provides a fluent interface for setting up neural network model parameters and architecture.
 */
public class LyraModelBuilder {
    private String modelID;
    private String modelAuthor;
    private String modelMetadata = "";
    private ArrayList<Integer> layers = new ArrayList<>();
    private ArrayList<Enums.activationFunctions> activationFunctionPerLayer = new ArrayList<>();
    private int backLayerSize = 0;
    private int frontLayerSize = 0;
    private Enums.activationFunctions backLayerActivationFunction;
    private Enums.IOType inputType;
    private Enums.IOType outputType;
    private Enums.activationFunctions activationFunction;

    /**
     * Sets the model identifier.
     *
     * @param s The name/identifier for the model
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder name(String s) {
        modelID = s;
        return this;
    }

    /**
     * Sets the model author's name.
     *
     * @param s The author's name
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder author(String s) {
        modelAuthor = s;
        return this;
    }

    /**
     * Sets the output data type for the model.
     *
     * @param s The output type from IOType enum
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder outputType(Enums.IOType s) {
        outputType = s;
        return this;
    }

    /**
     * Sets additional metadata for the model.
     *
     * @param md The metadata string
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder metadata(String md) {
        modelMetadata = md;
        return this;
    }

    /**
     * Sets the input data type for the model.
     *
     * @param s The input type from IOType enum
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder inputType(Enums.IOType s) {
        inputType = s;
        return this;
    }

    /**
     * Sets the activation function for all layers in the model.
     * Updates activation functions for any existing layers.
     *
     * @param s The activation function to use
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder setActivationFunction(Enums.activationFunctions s) {
        activationFunction = s;
        for (int i = 0; i < layers.size(); i++) {
            activationFunctionPerLayer.set(i, activationFunction);
        }
        return this;
    }

    /**
     * Sets the size of the output layer.
     *
     * @param s The number of neurons in the output layer
     * @return This builder instance for method chaining
     * @throws InvalidModelError if size is negative
     */
    public LyraModelBuilder backLayerSize(int s) {
        if (s < 0) {
            throw new InvalidModelError("BACK LAYER SIZE CAN NOT EQUAL ZERO");
        }
        backLayerSize = s;
        return this;
    }

    /**
     * Sets the size of the input layer.
     *
     * @param s The number of neurons in the input layer
     * @return This builder instance for method chaining
     * @throws InvalidModelError if size is negative
     */
    public LyraModelBuilder frontLayerSize(int s) {
        if (s < 0) {
            throw new InvalidModelError("FRONT LAYER SIZE CAN NOT EQUAL ZERO");
        }
        frontLayerSize = s;
        return this;
    }

    /**
     * Sets the complete model architecture using an ArrayList of layer sizes.
     *
     * @param architecture ArrayList containing the size of each layer
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder setModelArchitecture(ArrayList<Integer> architecture) {
        layers = architecture;
        return this;
    }

    /**
     * Sets the complete model architecture using an array of layer sizes.
     *
     * @param architecture Array containing the size of each layer
     * @return This builder instance for method chaining
     */
    public LyraModelBuilder setModelArchitecture(int[] architecture) {
        layers = new ArrayList<>();
        for (int size : architecture) {
            layers.add(size);
        }
        return this;
    }

    /**
     * Adds a hidden layer to the model with specified number of neurons.
     *
     * @param s The number of neurons in the new hidden layer
     * @return This builder instance for method chaining
     * @throws InvalidModelError if size is negative
     */
    public LyraModelBuilder addHiddenLayer(int s) {
        if (s < 0) {
            throw new InvalidModelError("HIDDEN SIZE CAN NOT EQUAL ZERO");
        }
        layers.add(s);
        activationFunctionPerLayer.add(activationFunction);
        return this;
    }

    /**
     * Builds and returns a configured LyraModel instance.
     * Validates all required parameters before building.
     *
     * @return A new LyraModel instance configured with the specified parameters
     * @throws InvalidModelError if any required parameters are missing or invalid:
     *         - If model ID or author is null
     *         - If no layers are defined
     *         - If activation functions are not properly set
     *         - If input/output types are null
     *         - If RAW input/output type is used without specifying layer sizes
     */
    public LyraModel build() {
        backLayerActivationFunction = activationFunction;

        if (modelID == null ||
                modelAuthor == null ||
                layers.isEmpty() ||
                backLayerActivationFunction == null ||
                activationFunctionPerLayer.isEmpty() ||
                activationFunctionPerLayer.size() != layers.size() ||
                inputType == null || outputType == null) {
            throw new InvalidModelError("ONE OR MORE FIELDS ARE MISSING FROM MODEL BUILDER!");
        }
        if (inputType == Enums.IOType.RAW && frontLayerSize == 0) {
            throw new InvalidModelError("IF THE DATATYPE \"RAW\" IS SELECTED FOR THE FIRST LAYER, YOU MUST SPECIFY THE FRONT LAYER SIZE!");
        }
        if (outputType == Enums.IOType.RAW && backLayerSize == 0) {
            throw new InvalidModelError("IF THE DATATYPE \"RAW\" IS SELECTED FOR THE LAST LAYER, YOU MUST SPECIFY THE LAST LAYER SIZE!");
        }
        if (modelMetadata.isEmpty() || modelMetadata.isBlank()) {
            modelMetadata = "NONE";
        }

        LyraModel model = new LyraModel();
        model = Initialization.initializeModel(model, modelID, modelAuthor, inputType,
                outputType, layers, activationFunctionPerLayer, frontLayerSize,
                backLayerSize, backLayerActivationFunction, activationFunction);
        Essentials.logger.logString("Model built!");
        return model;
    }
}


//===============================================================================================
//== This class is a builder that lets the user configure a model, including  the architecture,==
//== sets the input/output datatypes, name, author, metadata, activation function, and anything==
//== else that would be needed to build the model.                                             ==
//===============================================================================================

//Equinox Electronic