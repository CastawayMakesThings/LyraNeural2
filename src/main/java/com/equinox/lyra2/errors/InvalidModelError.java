package com.equinox.lyra2.errors;

public class InvalidModelError extends RuntimeException{
    public InvalidModelError(String message) {
        super(message);
    }
}
