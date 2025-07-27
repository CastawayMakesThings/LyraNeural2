package com.equinox.lyra2.api;

import com.equinox.lyra2.exceptions.LyraError;
import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;
import com.equinox.lyra2.objects.LyraModel;
import com.equinox.lyra2.processing.DatatypeConversion;
import com.equinox.lyra2.processing.Feeding;
import com.equinox.lyra2.processing.ModelChecker;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Running {
    public static Object feed(LyraModel model, Object input) {

        //Starts the feeder's executor service
        Feeding.startExecutor();

        ArrayList<Double> binaryData = new ArrayList<>();

        ModelChecker.checkModel(model);

        //Makes sure the inputted datatype matches the model's input type
        if(!DatatypeConversion.isValidDataType(model.frontLayer.inputType, input)) {
            throw new LyraError("ERROR, MODEL "+model.modelID+" TAKES IN A "+model.frontLayer.inputType.name()+", BUT RECEIVED A "+input.getClass().getSimpleName()+" INSTEAD!");
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
            throw new LyraError("COULD NOT FEED THE MODEL!" + e.getMessage() );
        }

        //Converts the binary output to the desired output.
        Object obj;
        try {
            obj = DatatypeConversion.convertFromBinaryArray(model.outputType, resultBinaryData);
        } catch (LyraWrongDatatypeException e) {
            throw new LyraError("ERROR, THERE WAS AN ISSUE CONVERTING THE BINARY OUTPUT TO THE DESIRED OUTPUT TYPE!" + e.getMessage() );
        }

        //End the feeder's executor service
        Feeding.endExecutor();

        return obj;
    }

    //This is for people who don't know that feeding a model is practically running it.
    public static Object run(LyraModel model, Object input) {
        return feed(model,input);
    }
}
