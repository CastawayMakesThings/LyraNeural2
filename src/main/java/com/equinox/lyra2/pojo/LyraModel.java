package com.equinox.lyra2.pojo;

import com.equinox.lyra2.Enums;

import java.util.ArrayList;
import java.util.HashSet;

public class LyraModel {
    public String modelID;
    public FrontLayer frontLayer;
    public ArrayList<Layer> layers;
    public String modelAuthor;
    public Enums.IOType outputType;
}
