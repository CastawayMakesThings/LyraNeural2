package io.github.equinoxelectronic.lyra2.api;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraError;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;
import io.github.equinoxelectronic.lyra2.objects.DataSet;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.DatatypeConversion;
import io.github.equinoxelectronic.lyra2.processing.Feeding;
import io.github.equinoxelectronic.lyra2.processing.ModelChecker;
import io.github.equinoxelectronic.lyra2.processing.Training;

import java.util.ArrayList;

/**
 * Builder class for configuring and executing neural network model training.
 * Provides a fluent interface for setting up training parameters and data.
 */
public class Trainer {
    private ArrayList<ArrayList<Double>> inputData;
    private ArrayList<ArrayList<Double>> outputData;
    private LyraModel model;
    private long epochsLimit;
    private boolean limitEpochs;
    private long timeLimit;
    private boolean limitTime;
    private double learningRate;
    private int statusPrintInterval;
    private double threshold = 0;
    private Enums.trainingStoppers primaryStopper;
    private boolean shouldUseProgressBar = false;

    /**
     * Sets the maximum number of training epochs.
     *
     * @param limit Maximum number of epochs to train
     * @return This trainer instance for method chaining
     */
    public Trainer setEpochLimit(long limit) {
        limitEpochs = true;
        epochsLimit = limit;
        return this;
    }

    /**
     * Sets the maximum training time in seconds.
     *
     * @param limit Maximum training time in seconds
     * @return This trainer instance for method chaining
     */
    public Trainer setTimeLimit(long limit) {
        limitTime = true;
        timeLimit = limit;
        return this;
    }

    /**
     * Sets the model to be trained.
     *
     * @param model The neural network model to train
     * @return This trainer instance for method chaining
     */
    public Trainer setModel(LyraModel model) {
        this.model = model;
        return this;
    }

    /**
     * Sets the primary criterion for stopping training.
     *
     * @param stopper The stopping criterion from trainingStoppers enum
     * @return This trainer instance for method chaining
     */
    public Trainer setPrimaryTrainingStopper(Enums.trainingStoppers stopper) {
        this.primaryStopper = stopper;
        return this;
    }

    /**
     * Sets the method for displaying training progress.
     *
     * @param statusPrintingMethod Either "progressBar"/"bar" or "intervals"/"terminal"
     * @return This trainer instance for method chaining
     * @throws LyraError if an invalid printing method is specified
     */
    public Trainer setStatusPrintingMethod(String statusPrintingMethod) {
        statusPrintingMethod = statusPrintingMethod.toLowerCase();
        switch (statusPrintingMethod) {
            case "progressbar":
            case "bar":
                shouldUseProgressBar = true;
                return this;
            case "intervals":
            case "terminal":
                shouldUseProgressBar = false;
                return this;
            default:
                throw new LyraError("Invalid status printing method!\n Use \"bar\" or \"intervals\".");
        }
    }

    /**
     * Sets the training input data.
     *
     * @param inputData List of input samples
     * @return This trainer instance for method chaining
     * @throws RuntimeException if data conversion fails
     */
    public Trainer setInputData(ArrayList<Object> inputData) {
        ArrayList<ArrayList<Double>> binaryObjects = new ArrayList<>();
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

    /**
     * Sets the training output (target) data.
     *
     * @param outputData List of output samples
     * @return This trainer instance for method chaining
     * @throws RuntimeException if data conversion fails
     */
    public Trainer setOutputData(ArrayList<Object> outputData) {
        ArrayList<ArrayList<Double>> binaryObjects = new ArrayList<>();
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

    /**
     * Sets both input and output training data from a DataSet object.
     *
     * @param ds DataSet containing both input and output samples
     * @return This trainer instance for method chaining
     * @throws RuntimeException if data conversion fails
     */
    public Trainer setTrainingData(DataSet ds) {
        ArrayList<ArrayList<Double>> binaryObjects = new ArrayList<>();
        for (Object o : ds.inputs) {
            try {
                binaryObjects.add(DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, o));
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        }
        this.inputData = binaryObjects;

        binaryObjects = new ArrayList<>();
        for (Object o : ds.outputs) {
            try {
                binaryObjects.add(DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, o));
            } catch (LyraWrongDatatypeException e) {
                throw new RuntimeException(e);
            }
        }
        this.outputData = binaryObjects;
        return this;
    }

    /**
     * Sets the interval for printing training status updates.
     *
     * @param statusPrintInterval Number of epochs between status prints
     * @return This trainer instance for method chaining
     */
    public Trainer setStatusPrintInterval(int statusPrintInterval) {
        this.statusPrintInterval = statusPrintInterval;
        return this;
    }

    /**
     * Sets the learning rate for training.
     *
     * @param learningRate The learning rate to use
     * @return This trainer instance for method chaining
     */
    public Trainer setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    /**
     * Sets the error threshold for early stopping.
     *
     * @param threshold Error threshold; training stops when error falls below this value
     * @return This trainer instance for method chaining
     */
    public Trainer setErrorThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    /**
     * Executes the training process with the configured parameters.
     * Validates all parameters before starting training.
     *
     * @return The trained model
     * @throws LyraError if:
     *         <ul>
     *           <li>Model is null</li>
     *           <li>Input/output data is null</li>
     *           <li>Learning rate is not positive</li>
     *           <li>Status print interval is negative</li>
     *           <li>Input/output data sizes don't match</li>
     *           <li>Input dimensions don't match model</li>
     *         </ul>
     */
    public LyraModel train() {
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

        Training.startExecutor();
        Feeding.startExecutor();

        try {
            return Training.trainModel(model, inputData, outputData, epochsLimit,
                    limitEpochs, limitTime, timeLimit, statusPrintInterval, learningRate,
                    threshold, shouldUseProgressBar, primaryStopper);
        } finally {
            Training.endExecutor();
            Feeding.endExecutor();
        }
    }

    /**
     * Returns this trainer instance for method chaining.
     *
     * @return This trainer instance
     */
    public Trainer configure() {
        return this;
    }
}


//===========================================================================
//== This class is pretty much a builder that configures info for training ==
//== a model, not much explanation is needed. Think of this as a higher-level==
//== layer to make configuring a trainer a little more elegant.             ==
//===========================================================================

//Equinox Electronic