package com.equinox.lyra2.processing;

import com.equinox.lyra2.objects.LyraModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class Saving {
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

        //Writes the model to the filepath.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(model, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model to " + filepath, e);
        }
    }
}
