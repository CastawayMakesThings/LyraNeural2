package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.Enums;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.Layer;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.objects.Neuron;

import java.util.ArrayList;

public class Training {
    public static LyraModel trainModel(LyraModel model,
                                       ArrayList<ArrayList<Double>> inputDataSet,
                                       ArrayList<ArrayList<Double>> wantedOutputDataSet,
                                       long epochs,
                                       boolean shouldLimitEpochs,
                                       boolean shouldLimitTime,
                                       long timeLimit,
                                       int statusPrintInterval,
                                       double learningRate,
                                       double errorThreshold) {

        Essentials.logger.logString("Starting model training...");
        ModelChecker.checkModel(model);
        int epoch = 0;
        int goodScoreStreak = 0;
        long startTimeInSeconds = System.currentTimeMillis() / 1000;
        double avgError;

        while (true) {
            double totalError = 0;

            // Iterate through each training example
            for (int i = 0; i < inputDataSet.size(); i++) {
                ArrayList<Double> input = inputDataSet.get(i);
                ArrayList<Double> target = wantedOutputDataSet.get(i);

                // Forward pass
                ArrayList<Double> output = null;
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
                    // TANH derivative with numerical stability
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
                            sum = clipGradient(sum + nextLayer.neurons.get(k).weights.get(j) * allDeltas.get(0).get(k), 1.0);
                        }
                        double activation = layerActivations.get(layerIdx + 1).get(j);

                        double derivative;
                        if (currentLayer.activationFunction == Enums.activationFunctions.LEAKY_RELU) {
                            derivative = activation > 0 ? 1.0 : 0.01;
                        } else if (currentLayer.activationFunction == Enums.activationFunctions.TANH) {
                            derivative = 1 - (activation * activation) + 1e-7;
                        } else if (currentLayer.activationFunction == Enums.activationFunctions.RELU) {
                            derivative = activation > 0 ? 1.0 : 0.0;  // ReLU derivative
                        } else {
                            throw new RuntimeException("Unsupported activation function");
                        }

                        double delta = clipGradient(sum * derivative, 1.0);
                        currentDeltas.add(delta);
                    }

                    allDeltas.add(0, currentDeltas);  // Move this outside the inner loop
                }

                // Update weights and biases
                for (int layerIdx = 0; layerIdx < model.layers.size(); layerIdx++) {
                    Layer currentLayer = model.layers.get(layerIdx);
                    ArrayList<Double> prevActivations = layerActivations.get(layerIdx);
                    ArrayList<Double> deltas = allDeltas.get(layerIdx);

                    //Loops through neurons
                    for (int j = 0; j < currentLayer.neurons.size(); j++) {
                        Neuron neuron = currentLayer.neurons.get(j);
                        double delta = clipGradient(deltas.get(j), 1.0);

                        // Update bias with clipping
                        double biasUpdate = clipGradient(learningRate * delta, 0.1);
                        neuron.bias += biasUpdate;

                        // Update weights with clipping
                        for (int k = 0; k < neuron.weights.size(); k++) {
                            double weightUpdate = clipGradient(learningRate * delta * prevActivations.get(k), 0.1);
                            neuron.weights.set(k, neuron.weights.get(k) + weightUpdate);
                        }
                    }

                }
            }

            avgError = totalError / (inputDataSet.size() * model.layers.getLast().neurons.size());

            // Print progress at the interval
            if(statusPrintInterval != 0) {
                if (epoch % statusPrintInterval == 0) {
                    Essentials.logger.logString(String.format("Epoch: %d, Time (in seconds): %d, Average Error: %.6f", epoch, (System.currentTimeMillis() / 1000) - startTimeInSeconds, avgError));
                }
            }
            if(totalError < errorThreshold) {
                goodScoreStreak++;
            }
            if(goodScoreStreak >= 5) {
                break;
            }
            if(epoch >= epochs && shouldLimitEpochs) {
                break;
            }
            if(shouldLimitTime && (System.currentTimeMillis() / 1000) - startTimeInSeconds >= timeLimit) {
                break;
            }
            epoch++;
        }

        Essentials.logger.logString("Training Completed! Average Error: " + avgError);

        return model;
    }
    private static double clipGradient(double value, double threshold) {
        return Math.max(Math.min(value, threshold), -threshold);
    }
}

