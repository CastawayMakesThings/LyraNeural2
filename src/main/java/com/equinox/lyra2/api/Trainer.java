package com.equinox.lyra2.api;

import com.equinox.lyra2.errors.LyraError;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.processing.DatatypeConversion;
import com.equinox.lyra2.processing.ModelChecker;
import com.equinox.lyra2.processing.Training;

import javax.xml.crypto.Data;
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

    public Trainer setEpochLimit(long limit) {
        limitEpochs = true;
        epochsLimit = limit;
        return this;
    }
    public Trainer setTimeLimit(long limit) {
        limitTime = true;
        timeLimit = limit;
        return this;
    }
    public Trainer setModel(LyraModel model) {
        this.model = model;
        return this;
    }
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
    public Trainer setStatusPrintInterval(int statusPrintInterval) {
        this.statusPrintInterval = statusPrintInterval;
        return this;
    }
    public Trainer setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }
    public Trainer setErrorThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }


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
        
        

        //Actual training
        LyraModel trainedModel;
        trainedModel = Training.trainModel(model, inputData, outputData, epochsLimit,
                limitEpochs, limitTime, timeLimit, statusPrintInterval, learningRate, threshold);
        return trainedModel;
    }

    public Trainer configure(){
        return this;
    }

}
