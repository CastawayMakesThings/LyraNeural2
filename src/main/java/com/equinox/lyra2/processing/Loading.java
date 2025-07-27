package com.equinox.lyra2.processing;

import com.equinox.lyra2.Config;
import com.equinox.lyra2.objects.LyraModel;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class Loading {
    public static LyraModel loadModel(String filepath) {
        //Checks if filepath is valid
        if(!(filepath.endsWith(".lyra"))) {
            filepath = filepath+".lyra";
        }
        
        //Attempts to deserialize the file.
        LyraModel model;
        try {
            Gson gson = new Gson();
            model = gson.fromJson(new FileReader(filepath), LyraModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model from " + filepath, e);
        }

        //Checks the lyra file
        if(model.lyraVersion != Config.version) {
            model = Versioning.updateModel(model);
        }

        return model;
    }
}

//This is a very basic class that loads up a saved model file. As you can see,
//it uses JSON. This is definetly suboptimal for larger models, so I intend to write
//my parser.

//Equinox Electronic
