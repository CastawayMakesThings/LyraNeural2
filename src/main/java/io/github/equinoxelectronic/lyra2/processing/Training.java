package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraError;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;
import io.github.equinoxelectronic.lyra2.objects.Layer;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.objects.Neuron;
import io.github.equinoxelectronic.equinox_essentials.Essentials;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Training {

    //The executor service
    private static ExecutorService executor;

    public static void startExecutor() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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



    public static LyraModel trainModel(LyraModel model,
                                       ArrayList<ArrayList<Double>> inputDataSet,
                                       ArrayList<ArrayList<Double>> wantedOutputDataSet,
                                       long epochs,
                                       boolean shouldLimitEpochs,
                                       boolean shouldLimitTime,
                                       long timeLimit,
                                       int statusPrintInterval,
                                       double learningRate,
                                       double errorThreshold,
                                       boolean progressBar,
                                       Enums.trainingStoppers primaryStopper) {

        Essentials.logger.logString("Starting model training...");
        ModelChecker.checkModel(model);
        int epoch = 0;
        int goodScoreStreak = 0;
        long startTimeInSeconds = System.currentTimeMillis() / 1000;
        double avgError;

        //Starts a progress bar
        if(progressBar) {
            switch (primaryStopper) {
                case EPOCH:
                    if(epochs <= 0) {throw new LyraError("Epoch count must be greater than 0");}
                    Essentials.logger.createProgressBar("Training", (int) epochs);
                    break;
                case TIME:
                    System.out.println(timeLimit);
                    if(timeLimit < 1) {throw new LyraError("Time limit must be larger than 0");}
                    Essentials.logger.createProgressBar("Training", (int) (timeLimit));
                    break;
                case ERROR:
                    if(errorThreshold <= 0) {throw new LyraError("Error threshold must be greater than 0");}
                    Essentials.logger.createProgressBar("Training", (int) (errorThreshold * 1000));
                    break;
            }
        }

        while (true) {
            double totalError = 0;

            // Iterate through each training example
            for (int i = 0; i < inputDataSet.size(); i++) {
                ArrayList<Double> input = inputDataSet.get(i);
                ArrayList<Double> target = wantedOutputDataSet.get(i);

                // Forward pass
                ArrayList<Double> output;
                try {
                    output = Feeding.feedForward(model, input);
                } catch (LyraWrongDatatypeException e) {
                    throw new RuntimeException(e);
                }

                // Store activations for backprop
                ArrayList<ArrayList<Double>> layerActivations = new ArrayList<>();
                layerActivations.add(input);
                for (Layer layer : model.layers) {
                    ArrayList<Double> layerOutput = new ArrayList<>();
                    for (Neuron neuron : layer.neurons) {
                        layerOutput.add(neuron.value);
                    }
                    layerActivations.add(layerOutput);
                }

                // Calculate output layer gradients
                Layer outputLayer = model.layers.getLast();
                ArrayList<Double> outputDeltas = new ArrayList<>();

                for (int j = 0; j < outputLayer.neurons.size(); j++) {
                    double error = target.get(j) - output.get(j);
                    double outVal = output.get(j);
                    double derivative = 1 - (outVal * outVal) + 1e-7;
                    double delta = clipGradient(error * derivative, 1.0);
                    outputDeltas.add(delta);
                    totalError += Math.pow(error, 2);
                }

                // Backpropagate through hidden layers
                ArrayList<ArrayList<Double>> allDeltas = new ArrayList<>();
                allDeltas.add(outputDeltas);

                for (int layerIdx = model.layers.size() - 2; layerIdx >= 0; layerIdx--) {
                    Layer currentLayer = model.layers.get(layerIdx);
                    Layer nextLayer = model.layers.get(layerIdx + 1);
                    ArrayList<Double> currentDeltas = new ArrayList<>();

                    for (int j = 0; j < currentLayer.neurons.size(); j++) {
                        double sum = 0.0;
                        for (int k = 0; k < nextLayer.neurons.size(); k++) {
                            sum = clipGradient(sum + nextLayer.neurons.get(k).weights.get(j) * 
                                allDeltas.get(0).get(k), 1.0);
                        }
                        double activation = layerActivations.get(layerIdx + 1).get(j);
                        double derivative;
                        if (currentLayer.activationFunction == Enums.activationFunctions.LEAKY_RELU) {
                            derivative = activation > 0 ? 1.0 : 0.01;
                        } else if (currentLayer.activationFunction == Enums.activationFunctions.TANH) {
                            derivative = 1 - (activation * activation) + 1e-7;
                        } else if (currentLayer.activationFunction == Enums.activationFunctions.RELU) {
                            derivative = activation > 0 ? 1.0 : 0.0;
                        } else {
                            throw new RuntimeException("Unsupported activation function");
                        }
                        double delta = clipGradient(sum * derivative, 1.0);
                        currentDeltas.add(delta);
                    }
                    allDeltas.add(0, currentDeltas);
                }

                // Parallel weight updates
                for (int layerIdx = 0; layerIdx < model.layers.size(); layerIdx++) {
                    Layer currentLayer = model.layers.get(layerIdx);
                    ArrayList<Double> prevActivations = layerActivations.get(layerIdx);
                    ArrayList<Double> deltas = allDeltas.get(layerIdx);

                    ArrayList<CompletableFuture<Void>> updateFutures = new ArrayList<>();

                    for (int j = 0; j < currentLayer.neurons.size(); j++) {
                        final int neuronIdx = j;
                        updateFutures.add(CompletableFuture.runAsync(() -> {
                            Neuron neuron = currentLayer.neurons.get(neuronIdx);
                            double delta = clipGradient(deltas.get(neuronIdx), 1.0);

                            // Update bias
                            double biasUpdate = clipGradient(learningRate * delta, 0.1);
                            neuron.bias += biasUpdate;

                            // Update weights
                            for (int k = 0; k < neuron.weights.size(); k++) {
                                double weightUpdate = clipGradient(learningRate * delta * 
                                    prevActivations.get(k), 0.1);
                                neuron.weights.set(k, neuron.weights.get(k) + weightUpdate);
                            }
                        }, executor));
                    }

                    CompletableFuture.allOf(updateFutures.toArray(new CompletableFuture[0])).join();
                }
            }

            avgError = totalError / (inputDataSet.size() * model.layers.getLast().neurons.size());

            // Print progress at the interval
            if(statusPrintInterval != 0 && !progressBar) {
                if (epoch % statusPrintInterval == 0) {
                    Essentials.logger.logString(String.format("Epoch: %d, Time (in seconds): %d, Average Error: %.6f", 
                        epoch, (System.currentTimeMillis() / 1000) - startTimeInSeconds, avgError));
                }
            }

            //Updates progress bar
            if(progressBar) {
                switch (primaryStopper) {
                    case EPOCH:
                        Essentials.logger.updateProgressBar(epoch, (int) epochs);
                        break;
                    case TIME:
                        Essentials.logger.updateProgressBar((int) ((System.currentTimeMillis() / 1000) - startTimeInSeconds), (int) timeLimit);
                        break;
                    case ERROR:
                        Essentials.logger.updateProgressBar((int) (totalError / (inputDataSet.size() * model.layers.getLast().neurons.size()) * 1000), (int) (errorThreshold * 1000));
                        break;
                    default:
                        throw new RuntimeException("Unknown stopper");
                }
            }

            //Checks to see if training should stop
            if(totalError < errorThreshold) {
                goodScoreStreak++;
            }
            if(goodScoreStreak >= 5) {
                Essentials.logger.logString("Ended training because of good score.");
                break;
            }
            if(epoch >= epochs && shouldLimitEpochs) {
                Essentials.logger.logString("Ended training because epoch limit reached.");
                break;
            }
            if(shouldLimitTime && (System.currentTimeMillis() / 1000) - startTimeInSeconds >= timeLimit) {
                Essentials.logger.logString("Ended training because time limit reached.");
                break;
            }
            epoch++;
        }

        Essentials.logger.updateProgressBar(100, 100);
        Essentials.logger.logString("Training Completed! Average Error: " + avgError);

        return model;
    }
    private static double clipGradient(double value, double threshold) {
        return Math.max(Math.min(value, threshold), -threshold);
    }

private static ArrayList<Double> parallelForwardPass(LyraModel model, ArrayList<Double> input) {
    // Set input layer values
    for (int i = 0; i < model.frontLayer.neurons.size(); i++) {
        model.frontLayer.neurons.get(i).value = input.get(i);
    }

    // Process each layer in sequence, but neurons within each layer in parallel
    for (int layerIdx = 0; layerIdx < model.layers.size(); layerIdx++) {
        Layer currentLayer = model.layers.get(layerIdx);
        ArrayList<CompletableFuture<Void>> neuronFutures = new ArrayList<>();

        // Get previous layer values
        ArrayList<Double> prevLayerValues = new ArrayList<>();
        if (layerIdx == 0) {
            for (int k = 0; k < model.frontLayer.neurons.size(); k++) {
                prevLayerValues.add(model.frontLayer.neurons.get(k).value);
            }
        } else {
            for (int k = 0; k < model.layers.get(layerIdx - 1).neurons.size(); k++) {
                prevLayerValues.add(model.layers.get(layerIdx - 1).neurons.get(k).value);
            }
        }

        // Process neurons in parallel
        final ArrayList<Double> finalPrevLayerValues = new ArrayList<>(prevLayerValues);
        for (int neuronIdx = 0; neuronIdx < currentLayer.neurons.size(); neuronIdx++) {
            final int finalNeuronIdx = neuronIdx;
            neuronFutures.add(CompletableFuture.runAsync(() -> {
                Neuron neuron = currentLayer.neurons.get(finalNeuronIdx);
                double value = 0;
                
                // Calculate weighted sum
                for (int k = 0; k < finalPrevLayerValues.size(); k++) {
                    value += finalPrevLayerValues.get(k) * neuron.weights.get(k);
                }
                
                // Add bias and apply activation
                value += neuron.bias;
                value = ActivationMethods.activate(value, currentLayer.activationFunction);
                neuron.value = value;
            }, executor));
        }

        // Wait for all neurons in this layer to complete
        CompletableFuture.allOf(neuronFutures.toArray(new CompletableFuture[0])).join();
    }

    // Collect output
    ArrayList<Double> output = new ArrayList<>();
    for (Neuron neuron : model.layers.getLast().neurons) {
        output.add(neuron.value);
    }
    return output;
}
}


//=================================================================================
//== This is probably to most complicated class in this entire API. This is the  ==
//== class responsible for the training of the model. It uses pretty conventional==
//== methods for training (except for the fact that this uses ArrayLists of      ==
//== ArrayLists, rather than matrices.). I do hope to let users implement their  ==
//== own loss hope to let users implement their own loss calculation function.   ==
//=================================================================================

//Equinox Electronic