package com.equinox.lyra2.processing;

import com.equinox.equinox_essentials.Essentials;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;
import com.equinox.lyra2.pojo.Layer;
import com.equinox.lyra2.pojo.LyraModel;
import com.equinox.lyra2.pojo.Neuron;

import java.util.ArrayList;

public class Training {
    public static LyraModel trainModel(LyraModel model,
                                       ArrayList<ArrayList<Double>> inputDataSet,
                                       ArrayList<ArrayList<Double>> wantedOutputDataSet,
                                       int epochs,
                                       double learningRate) {

        Essentials.logger.logString("Starting model training...");
        ModelChecker.checkModel(model);

        for (int epoch = 0; epoch < epochs; epoch++) {
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
                    double derivative = output.get(j) * (1 - output.get(j)); // Sigmoid derivative
                    outputDeltas.add(error * derivative);
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
                            sum += nextLayer.neurons.get(k).weights.get(j) * allDeltas.get(0).get(k);
                        }
                        double activation = layerActivations.get(layerIdx + 1).get(j);
                        double derivative = activation * (1 - activation); // Sigmoid derivative
                        currentDeltas.add(sum * derivative);
                    }
                    allDeltas.add(0, currentDeltas);
                }

                // Update weights and biases
                for (int layerIdx = 0; layerIdx < model.layers.size(); layerIdx++) {
                    Layer currentLayer = model.layers.get(layerIdx);
                    ArrayList<Double> prevActivations = layerActivations.get(layerIdx);
                    ArrayList<Double> deltas = allDeltas.get(layerIdx);

                    for (int j = 0; j < currentLayer.neurons.size(); j++) {
                        Neuron neuron = currentLayer.neurons.get(j);
                        double delta = deltas.get(j);

                        // Update bias
                        neuron.bias += learningRate * delta;

                        // Update weights
                        for (int k = 0; k < neuron.weights.size(); k++) {
                            double weightUpdate = learningRate * delta * prevActivations.get(k);
                            neuron.weights.set(k, neuron.weights.get(k) + weightUpdate);
                        }
                    }
                }
            }

            // Print progress every 1000 epochs
            if (epoch % 1000 == 0) {
                double avgError = totalError / (inputDataSet.size() * model.layers.getLast().neurons.size());
                Essentials.logger.logString(String.format("Epoch %d, Average Error: %.6f", epoch, avgError));
            }
        }

        return model;
    }
}

