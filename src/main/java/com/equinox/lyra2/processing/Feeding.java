package com.equinox.lyra2.processing;

import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.LyraModel;

import java.util.ArrayList;

public class Feeding {

    public static ArrayList<Double> feedForward(LyraModel model, ArrayList<Double> binaryData) throws LyraWrongDatatypeException {

        //Checks to make sure the model is valid
        ModelChecker.checkModel(model);

        //Loops through the neurons in the front layer and sets their value to the previously set input
        for (int i = 0; i < model.frontLayer.neurons.size(); i++) {
            model.frontLayer.neurons.get(i).value = binaryData.get(i);
        }

        //--BEGINNING OF ACTUAL FEEDFORWARD--
        //Loops through every layer in the model.
        for (int i = 0; i < model.layers.size(); i++) {

            //Loops through every neuron in the layer
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                double value = 0;
                int prevLayerSize;
                ArrayList<Double> prevLayerValues;

                // Determine the previous layer's size and values
                if (i == 0) {
                    prevLayerSize = model.frontLayer.neurons.size();
                    prevLayerValues = new ArrayList<>();
                    for (int k = 0; k < prevLayerSize; k++) {
                        prevLayerValues.add(model.frontLayer.neurons.get(k).value);
                    }
                } else {
                    prevLayerSize = model.layers.get(i-1).neurons.size();
                    prevLayerValues = new ArrayList<>();
                    for (int k = 0; k < prevLayerSize; k++) {
                        prevLayerValues.add(model.layers.get(i-1).neurons.get(k).value);
                    }
                }

                //Calculate weighted sum
                for (int k = 0; k < prevLayerSize; k++) {
                    value += prevLayerValues.get(k) * model.layers.get(i).neurons.get(j).weights.get(k);
                }

                // Add bias and apply activation function
                value += model.layers.get(i).neurons.get(j).bias;
                value = ActivationMethods.activate(value, model.layers.get(i).activationFunction);
                model.layers.get(i).neurons.get(j).value = value;
            }
        }

        //The raw output values
        ArrayList<Double> output = new ArrayList<>();

        //Gets the output values
        for (int i = 0; i < model.layers.getLast().neurons.size(); i++) {
            output.add(model.layers.getLast().neurons.get(i).value);
        }

        //---END OF ACTUAL FEEDFORWARD---
        return output;
    }
}

