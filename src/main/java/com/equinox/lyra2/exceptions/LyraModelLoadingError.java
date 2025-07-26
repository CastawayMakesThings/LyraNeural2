package com.equinox.lyra2.exceptions;

public class LyraModelLoadingError extends RuntimeException{
    public LyraModelLoadingError(String message) {
        super(message);
    }
}

//Obviously, this is for whenever there is an error deserializing a model.

//Equinox Electronic