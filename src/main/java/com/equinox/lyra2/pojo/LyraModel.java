package com.equinox.lyra2.pojo;

import com.equinox.lyra2.Enums;
import com.equinox.lyra2.api.LyraModelBuilder;
import com.equinox.lyra2.processing.Initialization;

import java.util.ArrayList;
import java.util.HashSet;

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
}
