package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Handles forward propagation (inference) in neural networks with parallel processing support.
 * This class implements the feed-forward algorithm for neural network computation,
 * utilizing parallel processing to accelerate neuron calculations within each layer.
 */
public class Feeding {

    /**
     * Thread pool executor for parallel neuron processing.
     * The pool size is set to the number of available processors for optimal performance.
     */
    public static ExecutorService executor;

    /**
     * Initializes the thread pool executor for parallel processing.
     * Should be called before performing any feed-forward operations.
     */
    public static void startExecutor() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Performs forward propagation through the neural network.
     * The process includes:
     * 1. Input layer initialization
     * 2. Layer-by-layer forward propagation with parallel neuron processing
     * 3. Output collection from the final layer
     *
     * The computation for each neuron involves:
     * - Calculating weighted sum of inputs
     * - Adding bias
     * - Applying activation function
     *
     * @param model The neural network model to process
     * @param binaryData Input data as binary values
     * @return Output values from the final layer
     * @throws LyraWrongDatatypeException if model validation fails
     */
    public static ArrayList<Double> feedForward(LyraModel model, ArrayList<Double> binaryData)
            throws LyraWrongDatatypeException {
        ModelChecker.checkModel(model);

        // Initialize input layer
        for (int i = 0; i < model.frontLayer.neurons.size(); i++) {
            model.frontLayer.neurons.get(i).value = binaryData.get(i);
        }

        // Process each layer
        for (int i = 0; i < model.layers.size(); i++) {
            final int layerIndex = i;
            ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

            // Get previous layer values
            ArrayList<Double> prevLayerValues = getPreviousLayerValues(model, i);
            final int prevLayerSize = prevLayerValues.size();

            // Process neurons in parallel
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                final int neuronIndex = j;
                final ArrayList<Double> finalPrevLayerValues = new ArrayList<>(prevLayerValues);

                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                                processNeuron(model, layerIndex, neuronIndex, finalPrevLayerValues, prevLayerSize),
                        executor
                );

                futures.add(future);
            }

            // Wait for all neurons in current layer to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        // Collect output values
        ArrayList<Double> output = new ArrayList<>();
        for (int i = 0; i < model.layers.getLast().neurons.size(); i++) {
            output.add(model.layers.getLast().neurons.get(i).value);
        }

        return output;
    }

    /**
     * Safely shuts down the thread pool executor.
     * Attempts graceful shutdown first, then forces shutdown if necessary.
     */
    public static void endExecutor() {
        if (executor != null) {
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

    /**
     * Retrieves values from the previous layer for processing.
     *
     * @param model The neural network model
     * @param currentLayer Index of the current layer
     * @return List of values from the previous layer
     */
    private static ArrayList<Double> getPreviousLayerValues(LyraModel model, int currentLayer) {
        ArrayList<Double> prevLayerValues = new ArrayList<>();
        if (currentLayer == 0) {
            for (int k = 0; k < model.frontLayer.neurons.size(); k++) {
                prevLayerValues.add(model.frontLayer.neurons.get(k).value);
            }
        } else {
            for (int k = 0; k < model.layers.get(currentLayer - 1).neurons.size(); k++) {
                prevLayerValues.add(model.layers.get(currentLayer - 1).neurons.get(k).value);
            }
        }
        return prevLayerValues;
    }

    /**
     * Processes a single neuron in the network.
     * Calculates weighted sum, adds bias, and applies activation function.
     *
     * @param model The neural network model
     * @param layerIndex Current layer index
     * @param neuronIndex Current neuron index
     * @param prevLayerValues Values from previous layer
     * @param prevLayerSize Size of previous layer
     */
    private static void processNeuron(LyraModel model, int layerIndex, int neuronIndex,
                                      ArrayList<Double> prevLayerValues, int prevLayerSize) {
        double value = 0;

        // Calculate weighted sum
        for (int k = 0; k < prevLayerSize; k++) {
            value += prevLayerValues.get(k) *
                    model.layers.get(layerIndex).neurons.get(neuronIndex).weights.get(k);
        }

        // Add bias and apply activation function
        value += model.layers.get(layerIndex).neurons.get(neuronIndex).bias;
        value = ActivationMethods.activate(value,
                model.layers.get(layerIndex).activationFunction);
        model.layers.get(layerIndex).neurons.get(neuronIndex).value = value;
    }
}


//This is a very simple feedforward. It loops through the layers, then loops through each
//neuron in the layer, then loops through the weights. I could have used matrix math,
//but for some reason I didn't. Maybe Lyra3 will have it. But for now, I will stick to
//plain old arithmetic.

//Equinox Electronic

