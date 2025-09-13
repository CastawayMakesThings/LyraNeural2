package io.github.equinoxelectronic.lyra2;

/**
 * Contains enumeration types used throughout the Lyra2 neural network framework.
 * Defines constants for activation functions, input/output data types, and training control.
 */
public class Enums {

    /**
     * Available activation functions for neural network layers.
     * <ul>
     *   <li>TANH - Hyperbolic tangent function, range [-1, 1]</li>
     *   <li>SIGMOID - Logistic function, range [0, 1]</li>
     *   <li>RELU - Rectified Linear Unit, max(0, x)</li>
     *   <li>LEAKY_RELU - Leaky ReLU, small slope for negative values</li>
     * </ul>
     */
    public enum activationFunctions {
        /** Hyperbolic tangent activation, range [-1, 1] */
        TANH,
        /** Logistic sigmoid activation, range [0, 1] */
        SIGMOID,
        /** Rectified Linear Unit, max(0, x) */
        RELU,
        /** Leaky ReLU, f(x) = x if x > 0, else 0.01x */
        LEAKY_RELU
    }

    /**
     * Supported data types for model input and output.
     * <ul>
     *   <li>RAW - Direct numerical values</li>
     *   <li>INTEGER - 32-bit integer values</li>
     *   <li>FLOAT - 32-bit floating point values</li>
     *   <li>DOUBLE - 64-bit floating point values</li>
     *   <li>LONG - 64-bit integer values</li>
     *   <li>CHAR - 16-bit character values</li>
     * </ul>
     */
    public enum IOType {
        /** Raw numerical values */
        RAW,
        /** 32-bit integer values */
        INTEGER,
        /** 32-bit floating point values */
        FLOAT,
        /** 64-bit floating point values */
        DOUBLE,
        /** 64-bit integer values */
        LONG,
        /** 16-bit character values */
        CHAR,
        /** 8-bit integer values */
        BYTE,
        /** 16-bit integer values */
        SHORT,
        /** 1-bit boolean values */
        BOOLEAN
    }

    /**
     * Training termination criteria.
     * <ul>
     *   <li>EPOCH - Stop after specified number of epochs</li>
     *   <li>TIME - Stop after specified time duration</li>
     *   <li>ERROR - Stop when error falls below threshold</li>
     * </ul>
     */
    public enum trainingStoppers {
        /** Stop training after specified number of epochs */
        EPOCH,
        /** Stop training after specified time duration */
        TIME,
        /** Stop training when error falls below threshold */
        ERROR
    }

    /**
     * Compute device selection for training/inference execution.
     * GPU is a placeholder and not yet implemented.
     */
    public enum computeDevices {
        /** Single-threaded CPU execution */
        CPU_SINGLE,
        /** Multi-threaded CPU execution */
        CPU_MULTI,
        /** GPU execution (not yet implemented) */
        GPU
    }
}


//A class that stores important enums.

//Equinox Electronic