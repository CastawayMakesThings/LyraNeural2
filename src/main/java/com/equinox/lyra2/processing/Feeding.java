
package com.equinox.lyra2.processing;

import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.LyraModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Feeding {

    public static ExecutorService executor;

    public static void startExecutor() {
        executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static ArrayList<Double> feedForward(LyraModel model, ArrayList<Double> binaryData)
            throws LyraWrongDatatypeException {

        //Checks to make sure the model is valid
        ModelChecker.checkModel(model);

        //Loops through the neurons in the front layer and sets their value to the previously set input
        for (int i = 0; i < model.frontLayer.neurons.size(); i++) {
            model.frontLayer.neurons.get(i).value = binaryData.get(i);
        }

        //--BEGINNING OF ACTUAL FEEDFORWARD--
        //Loops through every layer in the model.
        for (int i = 0; i < model.layers.size(); i++) {
            final int layerIndex = i;
            ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

            //Prepare previous layer data
            int prevLayerSize;
            ArrayList<Double> prevLayerValues = new ArrayList<>();

            if (i == 0) {
                prevLayerSize = model.frontLayer.neurons.size();
                for (int k = 0; k < prevLayerSize; k++) {
                    prevLayerValues.add(model.frontLayer.neurons.get(k).value);
                }
            } else {
                prevLayerSize = model.layers.get(i - 1).neurons.size();
                for (int k = 0; k < prevLayerSize; k++) {
                    prevLayerValues.add(model.layers.get(i - 1).neurons.get(k).value);
                }
            }

            //Process neurons in parallel
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                final int neuronIndex = j;
                final ArrayList<Double> finalPrevLayerValues = new ArrayList<>(prevLayerValues);
                final int finalPrevLayerSize = prevLayerSize;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    double value = 0;

                    //Calculate weighted sum
                    for (int k = 0; k < finalPrevLayerSize; k++) {
                        value += finalPrevLayerValues.get(k) *
                                model.layers.get(layerIndex).neurons.get(neuronIndex).weights.get(k);
                    }

                    // Add bias and apply activation function
                    value += model.layers.get(layerIndex).neurons.get(neuronIndex).bias;
                    value = ActivationMethods.activate(value,
                            model.layers.get(layerIndex).activationFunction);
                    model.layers.get(layerIndex).neurons.get(neuronIndex).value = value;
                }, executor);

                futures.add(future);
            }

            // Wait for all neurons in this layer to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
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

    public static void endExecutor() {
        if(executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }

}

//This is a very simple feedforward. It loops through the layers, then loops through each
//neuron in the layer, then loops through the weights. I could have used matrix math,
//but for some reason I didn't. Maybe Lyra3 will have it. But for now, I will stick to
//plain old arithmetic.

//Equinox Electronic

