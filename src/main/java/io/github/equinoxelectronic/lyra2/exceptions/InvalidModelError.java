package io.github.equinoxelectronic.lyra2.exceptions;

public class InvalidModelError extends RuntimeException{
    public InvalidModelError(String message) {
        super(message);
    }
}

//This error for whenever a model is loaded or used somewhere, but is invalid.

//Equinox Electronic