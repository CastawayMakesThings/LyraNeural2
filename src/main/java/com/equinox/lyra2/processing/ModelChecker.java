package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.errors.InvalidModelError;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.objects.Neuron;

import static com.equinox.lyra2.Config.VERBOSE;

public class ModelChecker {
    //------Checks to make sure model is valid------
    public static void checkModel(LyraModel model) {

        Essentials.logger.logString("Checking model...");
        Essentials.logger.logVerbose("Checking entire model...", VERBOSE);

        //Assures model exists
        if(model == null) {
            throw new InvalidModelError("Model is null");
        }

        //Makes sure the model data is not null
        if(model.frontLayer == null || model.layers == null || model.modelID == null) {
            throw new InvalidModelError("One or more model fields are null");
        }

        //Makes sure there are enough layers
        if(!(model.layers.size() > 2)) {
            throw new InvalidModelError("Model does not have enough layers to be fed.");
        }

        Essentials.logger.logVerbose("Checking each layer... ", VERBOSE);

        //Checks every layer in the model
        for (int i = 0; i < model.layers.size(); i++) {

            //Checks if the layer is null
            if(model.layers.get(i) == null) {
                throw new InvalidModelError("Layer "+i+" is null!");
            }

            //Checks if the layer has enough neurons
            if(model.layers.get(i).neurons.isEmpty()){
                throw new InvalidModelError("Layer "+i+" has no neurons!");
            }

            //Checks if the layer has a valid activation function
            if(model.layers.get(i).activationFunction == null) {
                throw new InvalidModelError("Layer "+i+" has no activation function!");
            }

            Essentials.logger.logVerbose("Checking each neuron in layer "+i+"...", VERBOSE);

            //Loops through every neuron in the layer to check it
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {

                //Helper object to reduce amount of code
                Neuron thisNeuron = model.layers.get(i).neurons.get(j);

                //Checks if any neurons are null
                if(thisNeuron == null) {
                    throw new InvalidModelError("Neuron "+j+" in layer "+i+" is null!");
                }

                //Checks if the neuron has any weights
                if(thisNeuron.weights == null) {
                    throw new InvalidModelError("Neuron "+j+" in layer "+i+"'s weights are null!");
                }

                //Checks if the neuron has a bias
                if(Double.isNaN(thisNeuron.bias)) {
                    throw new InvalidModelError("Neuron "+j+" in layer "+i+"'s bias is NaN!");
                }

                //Checks to make sure that the neuron count in the last layer matches the amount of weights in the neuron.
                if (i == 0) {
                    if(!(thisNeuron.weights.size() == model.frontLayer.neurons.size())) {
                        throw new InvalidModelError("Neuron "+j+" in layer "+i+"'s weight count does not match the amount of neurons in the front layer! \n ("+thisNeuron.weights.size()+" != "+model.frontLayer.neurons.size()+")\n\n");
                    }
                } else {
                    if(!(thisNeuron.weights.size() == model.layers.get(i - 1).neurons.size())) {
                        throw new InvalidModelError("Neuron "+j+" in layer "+i+"'s weight count does not match the amount of neurons in the previous layer! \n ("+thisNeuron.weights.size()+" != "+model.layers.get(i - 1).neurons.size()+")\n\n");
                    }
                }

                Essentials.logger.logVerbose("Checking each weight in neuron "+j+" in layer "+i+"...", VERBOSE);

                //Loops through every weight in the neuron to check it
                for (int k = 0; k < thisNeuron.weights.size(); k++) {

                    //Checks if any weights are NaN
                    if(Double.isNaN(thisNeuron.weights.get(k))) {
                        throw new InvalidModelError("Weight "+k+" in neuron "+j+" in layer "+i+" is NaN!");
                    }

                }

            }

        }

        Essentials.logger.logString("Model is valid!");
        Essentials.logger.logVerbose("Model has passed all checks!", VERBOSE);

    }

    //Checks to make sure the input macthes the model's input type
    public static void checkInputType (LyraModel model, Object input) {
        Essentials.logger.logVerbose("Checking input...", VERBOSE);
        if (model == null) {
            throw new InvalidModelError("Model is null");
        }
        if (input == null) {
            throw new InvalidModelError("Input is null");
        }
        if (model.frontLayer == null || model.frontLayer.inputType == null) {
            throw new InvalidModelError("Model front layer or input type is null");
        }

        switch (model.frontLayer.inputType) {
            case RAW -> {
                //Raw type accepts any input
            }
            case INTEGER -> {
                if (!(input instanceof Integer)) {
                    throw new InvalidModelError("Input must be an Integer");
                }
            }
            case FLOAT -> {
                if (!(input instanceof Float)) {
                    throw new InvalidModelError("Input must be a Float");
                }
            }
            case DOUBLE -> {
                if (!(input instanceof Double)) {
                    throw new InvalidModelError("Input must be a Double");
                }
            }
            case LONG -> {
                if (!(input instanceof Long)) {
                    throw new InvalidModelError("Input must be a Long");
                }
            }
            case CHAR -> {
                if (!(input instanceof Character)) {
                    throw new InvalidModelError("Input must be a Character");
                }
            }
            default -> throw new InvalidModelError("Unknown input type");
        }
    }
}
