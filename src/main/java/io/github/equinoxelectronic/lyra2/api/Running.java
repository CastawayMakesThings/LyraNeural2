package io.github.equinoxelectronic.lyra2.api;

import io.github.equinoxelectronic.lyra2.exceptions.LyraError;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.DatatypeConversion;
import io.github.equinoxelectronic.lyra2.processing.Feeding;
import io.github.equinoxelectronic.lyra2.processing.ModelChecker;

import java.util.ArrayList;

/**
 * Utility class for executing neural network model inference.
 * Provides methods to run (feed) input data through a trained LyraModel and obtain predictions.
 */
public class Running {

    /**
     * Feeds input data through a trained neural network model and returns the predicted output.
     * The process includes:
     * <ol>
     *   <li>Input validation and type checking</li>
     *   <li>Conversion of input to binary representation</li>
     *   <li>Forward propagation through the network</li>
     *   <li>Conversion of binary output to desired type</li>
     * </ol>
     *
     * @param model The trained LyraModel to use for inference
     * @param input The input data to process (must match the model's expected input type)
     * @return The model's output prediction, converted to the appropriate type
     * @throws LyraError if:
     *         <ul>
     *           <li>Input type doesn't match model's expected input type</li>
     *           <li>Error occurs during datatype conversion</li>
     *           <li>Error occurs during model feeding</li>
     *           <li>Error occurs during output conversion</li>
     *         </ul>
     */
    public static Object feed(LyraModel model, Object input) {
        //Starts the feeder's executor service
        Feeding.startExecutor();

        ArrayList<Double> binaryData = new ArrayList<>();

        ModelChecker.checkModel(model);

        //Makes sure the inputted datatype matches the model's input type
        if(!DatatypeConversion.isValidDataType(model.frontLayer.inputType, input)) {
            throw new LyraError("ERROR, MODEL " + model.modelID + " TAKES IN A " +
                    model.frontLayer.inputType.name() + ", BUT RECEIVED A " +
                    input.getClass().getSimpleName() + " INSTEAD!");
        }

        //Converts the object into a list of binary data
        try {
            binaryData = DatatypeConversion.convertToBinaryArray(model.frontLayer.inputType, input);
        } catch (LyraWrongDatatypeException e) {
            throw new LyraError("COULD NOT CONVERT DATATYPE FOR FEEDING!" + e.getMessage());
        }

        //Feeds the model
        ArrayList<Double> resultBinaryData;
        try {
            resultBinaryData = Feeding.feedForward(model, binaryData);
        } catch (LyraWrongDatatypeException e) {
            throw new LyraError("COULD NOT FEED THE MODEL!" + e.getMessage());
        }

        //Converts the binary output to the desired output
        Object obj;
        try {
            obj = DatatypeConversion.convertFromBinaryArray(model.outputType, resultBinaryData);
        } catch (LyraWrongDatatypeException e) {
            throw new LyraError("ERROR, THERE WAS AN ISSUE CONVERTING THE BINARY OUTPUT TO THE DESIRED OUTPUT TYPE!" + e.getMessage());
        }

        //End the feeder's executor service
        Feeding.endExecutor();

        return obj;
    }

    /**
     * Alias method for {@link #feed(LyraModel, Object)}.
     * Provided for more intuitive API usage when "running" a model.
     *
     * @param model The trained LyraModel to use for inference
     * @param input The input data to process
     * @return The model's output prediction
     * @throws LyraError if any error occurs during model execution
     * @see #feed(LyraModel, Object)
     */
    public static Object run(LyraModel model, Object input) {
        return feed(model, input);
    }
}


//A simple facade class for feeding the model

//Equinox Electronic
