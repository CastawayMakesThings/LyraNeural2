package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.ModelChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.equinoxelectronic.equinox_essentials.Essentials;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Saving {
    public static void saveModelAsJSON(String filepath, LyraModel model) {
        //Checks to make sure model is usable
        ModelChecker.checkModel(model);

        //----Modifies path to contain whole path----
        //Trims the filepath
        filepath = filepath.trim();
        //Checks to see if filePath contains the filename or just the directory
        if(filepath.endsWith("/") || filepath.endsWith("\\")) {
            filepath = filepath+ model.modelID+".lyra";
        }
        //Checks to see if the filePath contains the extension
        if(!filepath.endsWith(".lyra")) {
            filepath = filepath+".lyra";
        }

        //Writes the model to the filepath.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(model, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model to " + filepath, e);
        }
    }
    public static void saveModel(String filepath, LyraModel model) {
        //Checks to make sure model is usable
        ModelChecker.checkModel(model);

        //----Modifies path to contain whole path----
        //Trims the filepath
        filepath = filepath.trim();
        //Checks to see if filePath contains the filename or just the directory
        if(filepath.endsWith("/") || filepath.endsWith("\\")) {
            filepath = filepath+ model.modelID+".lyra";
        }
        //Checks to see if the filePath contains the extension
        if(!filepath.endsWith(".lyra")) {
            filepath = filepath+".lyra";
        }

        String serializedModel = Serializer.serializeModel(model);

        try {
            serializedModel = compressString(serializedModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(serializedModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model to " + filepath, e);
        }
        Essentials.logger.logString("Saved model to "+filepath +" succesfully!");
    }

    public static String compressString(String input) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Create XZ output stream with maximum compression
            LZMA2Options options = new LZMA2Options(LZMA2Options.PRESET_MAX);
            try (XZOutputStream xzOut = new XZOutputStream(baos, options)) {
                xzOut.write(input.getBytes(StandardCharsets.UTF_8));
            }
            // Convert to Base64 for easy string handling
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

}


//A very simple class that saves a model into a JSON file. As mentioned in the comments
//for Loading.java, this is less than ideal for large models, so I do intend to write my
//own serializer to save models.

//Equinox Electronic