package com.equinox.lyra2;

public class Enums {
    //The list of activation functions.
    public enum activationFunctions {TANH, SIGMOID, RELU, LEAKY_RELU}
    //The datatypes that Lyra2 models can take in or out.
    public enum IOType {RAW, INTEGER, FLOAT, DOUBLE, LONG, CHAR}
    //The ways of detecting when to stop training
    public enum trainingStoppers {EPOCH, TIME, ERROR}
}

//A class that stores important enums.

//Equinox Electronic