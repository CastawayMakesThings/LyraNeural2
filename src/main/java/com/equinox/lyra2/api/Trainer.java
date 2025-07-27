package com.equinox.lyra2.api;

import com.equinox.lyra2.exceptions.LyraError;
import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.processing.DatatypeConversion;
import com.equinox.lyra2.processing.Feeding;
import com.equinox.lyra2.processing.ModelChecker;
import com.equinox.lyra2.processing.Training;

import java.util.ArrayList;

public class Trainer {
    private ArrayList<ArrayList<Double>> inputData;
    private ArrayList<ArrayList<Double>> outputData;
    private LyraModel model;
    private long epochsLimit; private boolean limitEpochs;
    private long timeLimit; private boolean limitTime;
    private double learningRate;
    private int statusPrintInterval;
    private double threshold = 0;

    //The Epoch limit for training
    public Trainer setEpochLimit(long limit) {
        limitEpochs = true;
        epochsLimit = limit;
        return this;
    }

    //The time limit for training
    public Trainer setTimeLimit(long limit) {
        limitTime = true;
        timeLimit = limit;
        return this;
    }

    //The model to be trained
    public Trainer setModel(LyraModel model) {
        this.model = model;
        return this;
    }

    //Sets the input data set
    public Trainer setInputData(ArrayList<Object> inputData) {
        ArrayList<ArrayList<Double>> binaryObjects = new ArrayList<>();
        //Converts the object into binary
        for (Object o : inputData) {
            try {
                binaryObjects.add(DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, o));
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        }
        this.inputData = binaryObjects;
        return this;
    }

    //Sets the output dataset
    public Trainer setOutputData(ArrayList<Object> outputData) {
        ArrayList<ArrayList<Double>> binaryObjects = new ArrayList<>();
        //Converts the objects in binary
        for (Object o : outputData) {
            try {
                binaryObjects.add(DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, o));
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        }
        this.outputData = binaryObjects;
        return this;
    }

    //Sets the interval to print status messages
    public Trainer setStatusPrintInterval(int statusPrintInterval) {
        this.statusPrintInterval = statusPrintInterval;
        return this;
    }

    //Sets the learning rate for training
    public Trainer setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    //The error threshold. When this is reached, training will automatically stop.
    public Trainer setErrorThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    //The actual training method
    public LyraModel train() {
        //Some basic checks
        if (model == null) {
            throw new LyraError("Model cannot be null");
        }
        if (inputData == null || outputData == null) {
            throw new LyraError("Input and output data cannot be null");
        }
        if (learningRate <= 0) {
            throw new LyraError("Learning rate must be positive");
        }
        if (statusPrintInterval < 0) {
            throw new LyraError("Status print interval must be positive");
        }
        if (inputData.size() != outputData.size()) {
            throw new LyraError("Input and output data sizes must match");
        }
        if (inputData.getFirst().size() != model.frontLayer.neurons.size()) {
            throw new LyraError("Input data dimensions must match model input layer");
        }
        ModelChecker.checkModel(model);

        //Fires up the executor service
        Training.startExecutor();
        Feeding.startExecutor();

        try {
            //Actual training
            LyraModel trainedModel;
            trainedModel = Training.trainModel(model, inputData, outputData, epochsLimit,
                    limitEpochs, limitTime, timeLimit, statusPrintInterval, learningRate, threshold);
            return trainedModel;
        } finally {
            // Ensure executors are shut down
            Training.endExecutor();
            Feeding.endExecutor();

        }
    }

    //This method returns this builder
    public Trainer configure(){
        return this;
    }
}

//===========================================================================
//== This class is pretty much a builder that configures info for training ==
//== a model, not much explanation is needed. Think of this as a higher-level==
//== layer to make configuring a trainer a little more elegant.             ==
//===========================================================================

//Equinox Electronic