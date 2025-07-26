package com.equinox.lyra2.api;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.Enums;
import com.equinox.lyra2.errors.InvalidModelError;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.processing.Initialization;

import java.util.ArrayList;

public class LyraModelBuilder {
    private String modelID;
    private String modelAuthor;
    private ArrayList<Integer> layers = new ArrayList<>();
    private ArrayList<Enums.activationFunctions> activationFunctionPerLayer = new ArrayList<>();
    private int backLayerSize = 0;
    private int frontLayerSize = 0;
    private Enums.activationFunctions backLayerActivationFunction;
    private Enums.IOType inputType;
    private Enums.IOType outputType;
    private Enums.activationFunctions activationFunction;

    public LyraModelBuilder name(String s) {
        modelID = s;
         return this;
    }
    public LyraModelBuilder author(String s) {
        modelAuthor = s;
        return this;
    }

    public LyraModelBuilder outputType(Enums.IOType s) {
        outputType = s;
        return this;
    }
    public LyraModelBuilder inputType(Enums.IOType s) {
        inputType = s;
        return this;
    }
    public LyraModelBuilder setActivationFunction(Enums.activationFunctions s) {
        activationFunction = s;

        //Sets all previous layers to new activation function.
        for (int i = 0; i < layers.size(); i++) {
            activationFunctionPerLayer.set(i, activationFunction);
        }
        return this;
    }
    public LyraModelBuilder backLayerSize(int s) {
        if (s < 0) {throw new InvalidModelError("BACK LAYER SIZE CAN NOT EQUAL ZERO");}
        backLayerSize = s;
        return this;
    }
    public LyraModelBuilder frontLayerSize(int s) {
        if (s < 0) {throw new InvalidModelError("FRONT LAYER SIZE CAN NOT EQUAL ZERO");}
        return this;
    }
    public LyraModelBuilder setModelArchitecture(ArrayList<Integer> architecture) {
        layers = architecture;
        return this;
    }

    public LyraModelBuilder setModelArchitecture(int[] architecture) {
        layers = new ArrayList<>();
        for (int size : architecture) {
            layers.add(size);
        }
        return this;
    }

    public LyraModelBuilder addHiddenLayer(int s) {
        if (s < 0) {throw new InvalidModelError("HIDDEN SIZE CAN NOT EQUAL ZERO");}
        layers.add(s);
        activationFunctionPerLayer.add(activationFunction);
        return this;
    }
    public LyraModel build() {
        backLayerActivationFunction = activationFunction;
        //Checks to make sure all required fields are entered
        if(modelID == null ||
                modelAuthor == null ||
                layers.isEmpty() ||
                backLayerActivationFunction == null ||
                activationFunctionPerLayer.isEmpty() ||
                activationFunctionPerLayer.size() !=
                        layers.size() ||
                inputType == null || outputType == null) {
            throw new InvalidModelError("ONE OR MORE FIELDS ARE MISSING FROM MODEL BUILDER! functionperlayer: "+activationFunctionPerLayer.size()+" layers: "+layers.size());
        }
        if(inputType == Enums.IOType.RAW && frontLayerSize == 0) {
            throw new InvalidModelError("IF THE DATATYPE \"RAW\" IS SELECTED FOR THE FIRST LAYER, YOU MUST SPECIFY THE FRONT LAYER SIZE!");
        }
        if(outputType == Enums.IOType.RAW && backLayerSize == 0) {
            throw new InvalidModelError("IF THE DATATYPE \"RAW\" IS SELECTED FOR THE LAST LAYER, YOU MUST SPECIFY THE LAST LAYER SIZE!");
        }

        //Actually builds the model
        LyraModel model = new LyraModel();
        model = Initialization.initializeModel(model, modelID, modelAuthor, inputType, outputType, layers, activationFunctionPerLayer, frontLayerSize, backLayerSize, backLayerActivationFunction);
        Essentials.logger.logString("Model built!");
        return model;
    }
}
