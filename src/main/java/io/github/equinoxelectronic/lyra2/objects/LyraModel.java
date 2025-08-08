package io.github.equinoxelectronic.lyra2.objects;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.api.LyraModelBuilder;
import io.github.equinoxelectronic.lyra2.processing.lyraFile.Loading;
import io.github.equinoxelectronic.lyra2.processing.lyraFile.Saving;

import java.util.ArrayList;

/**
 * Represents a neural network model in the Lyra2 framework.
 * Contains all components and metadata necessary for model operation,
 * including layers, data types, and configuration information.
 */
public class LyraModel {
    /**
     * Unique identifier for the model.
     */
    public String modelID;

    /**
     * The input layer of the neural network.
     * Handles initial data processing and type validation.
     */
    public FrontLayer frontLayer;

    /**
     * List of hidden and output layers in the network.
     * Does not include the front (input) layer.
     */
    public ArrayList<Layer> layers;

    /**
     * Name of the model's creator or maintainer.
     */
    public String modelAuthor;

    /**
     * Expected data type for model outputs.
     */
    public Enums.IOType outputType;

    /**
     * Version of Lyra framework used to create the model.
     * Used for compatibility checking during model loading.
     */
    public String lyraVersion;

    /**
     * Optional metadata for storing additional model information.
     */
    public String metadata;

    /**
     * Global activation function for all layers.
     * Replaces individual layer activation functions.
     */
    public Enums.activationFunctions activationFunction;

    /**
     * Creates a new model builder for configuring this model.
     *
     * @return A new LyraModelBuilder instance
     */
    public LyraModelBuilder builder() {
        return new LyraModelBuilder();
    }

    /**
     * Saves the model to a file in Lyra format.
     * Persists all model components and configuration.
     *
     * @param filepath Path where the model should be saved
     */
    public void save(String filepath) {
        Saving.saveModel(filepath, this);
    }

    /**
     * Loads a model from a Lyra format file.
     * Updates all current model components with loaded data.
     *
     * @param filepath Path to the model file to load
     */
    public void load(String filepath) {
        LyraModel loaded = Loading.loadModel(filepath);
        this.modelID = loaded.modelID;
        this.frontLayer = loaded.frontLayer;
        this.layers = loaded.layers;
        this.modelAuthor = loaded.modelAuthor;
        this.outputType = loaded.outputType;
        this.lyraVersion = loaded.lyraVersion;
        this.metadata = loaded.metadata;
        this.activationFunction = loaded.activationFunction;
    }
}



//This class is the model class. All models are an instance of this class. Note that metadata, modelID, and modelAuthor
//don't have any effect on the actual model, and are all really just metadata.

//Equinox Electronic

