package com.equinox.lyra2.objects;

import com.equinox.lyra2.Enums;
import com.equinox.lyra2.api.LyraModelBuilder;
import com.equinox.lyra2.processing.Loading;
import com.equinox.lyra2.processing.Saving;

import java.util.ArrayList;

public class LyraModel {
    //The name of the model
    public String modelID;
    //The first layer of the model
    public FrontLayer frontLayer;
    //The rest of the layers in the model
    public ArrayList<Layer> layers;
    //The model author
    public String modelAuthor;
    //The output datatype
    public Enums.IOType outputType;
    //The version of Lyra that this model was made in. This is not used anywhere except for saving/loading.
    public String lyraVersion;
    //Any metadata somebody wants to add to their model.
    public String metadata;

    //This method returns the LyraModelBuilder to configure this model
    public LyraModelBuilder builder(){
        return new LyraModelBuilder();
    }

    //Saves this model
    public void save(String filepath) {
        Saving.saveModel(filepath, this);
    }

    //Loads this model
    public void load(String filepath) {
        LyraModel loaded = Loading.loadModel(filepath);
        this.modelID = loaded.modelID;
        this.frontLayer = loaded.frontLayer;
        this.layers = loaded.layers;
        this.modelAuthor = loaded.modelAuthor;
        this.outputType = loaded.outputType;
        this.lyraVersion = loaded.lyraVersion;
        this.metadata = loaded.metadata;
    }
}


//This class is the model class. //TODO Finish this class's documentation