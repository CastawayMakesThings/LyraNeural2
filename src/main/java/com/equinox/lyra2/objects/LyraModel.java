package com.equinox.lyra2.objects;

import com.equinox.lyra2.Enums;
import com.equinox.lyra2.api.LyraModelBuilder;
import com.equinox.lyra2.processing.Loading;
import com.equinox.lyra2.processing.Saving;

import java.util.ArrayList;

public class LyraModel {
    public String modelID;
    public FrontLayer frontLayer;
    public ArrayList<Layer> layers;
    public String modelAuthor;
    public Enums.IOType outputType;
    public String lyraVersion;
    public String metadata;

    public LyraModelBuilder builder(){
        return new LyraModelBuilder();
    }
    public void save(String filepath) {
        Saving.saveModel(filepath, this);
    }
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
