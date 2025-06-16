package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.Config;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;
import com.equinox.lyra2.pojo.LyraModel;

import java.util.ArrayList;

public class Feeding {

    public static ArrayList<Double> feedForward(LyraModel model, Object input) throws LyraWrongDatatypeException {
        Essentials.logger.logString("Feeding forward...");

        //Checks to make sure the model is valid
        ModelChecker.checkModel(model);

        //Convert the input into binary
        ModelChecker.checkInputType(model, input);
        ArrayList<Double> binaryData;
        binaryData = DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, input);

        Essentials.logger.logVerbose("Input converted to binary...", Config.VERBOSE);

        //Loops through the neurons in the front layer and sets their value to the previously set input
        for (int i = 0; i < model.frontLayer.neurons.size(); i++) {
            model.frontLayer.neurons.get(i).value = binaryData.get(i);
        }

        Essentials.logger.logVerbose("Input set to neurons...", Config.VERBOSE);

        //--BEGINNING OF ACTUAL FEEDFORWARD--
        //Loops through every layer in the model.
        for (int i = 0; i < model.layers.size(); i++) {
            Essentials.logger.logVerbose("Feeding layer "+i+"...", Config.VERBOSE);

            //Loops through every neuron in the layer
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                double value = 0;

                //Loops through every weight in the neuron
                for (int k = 0; k < model.layers.get(i).neurons.get(j).weights.size(); k++) {
                    //Checks if it is the first layer
                    if(i > 0) {
                        //If it is not the first layer, multiply the values of the previous layer by this layer's weights
                        value += model.layers.get(i - 1).neurons.get(k).value * model.layers.get(i).neurons.get(j).weights.get(k);
                    } else {
                        //If it is, multiply the values of the front layer by this layer's weights'
                        value += model.frontLayer.neurons.get(k).value * model.layers.get(i).neurons.get(j).weights.get(k);
                    }
                }
                value += model.layers.get(i).neurons.get(j).bias;
                value = ActivationMethods.activate(value, model.layers.get(i).activationFunction);
                model.layers.get(i).neurons.get(j).value = value;
            }
        }

        //The raw output values
        ArrayList<Double> output = new ArrayList<>();

        //Gets the output binary.
        for (int i = 0; i < model.layers.getLast().neurons.size(); i++) {
            output.add(model.layers.getLast().neurons.get(i).value);
        }

        //---END OF ACTUAL FEEDFORWARD---
        return output;
    }
}
