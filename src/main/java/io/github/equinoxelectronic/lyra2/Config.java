package io.github.equinoxelectronic.lyra2;

/**
 * Global configuration settings for the Lyra2 neural network framework.
 * Contains version information, logging settings, and file format specifications.
 */
public class Config {

    /**
     * Controls verbose logging output.
     * When true, provides detailed logging of operations and debug information.
     * When false, only essential messages are logged.
     */
    public static boolean VERBOSE = false;

    /**
     * The current version of the Lyra2 framework.
     * Used for compatibility checking and version tracking.
     * Format: MAJOR.MINOR.PATCH
     */
    public static final String version = "1.0.0";

    /**
     * Version of the model file parser/serializer.
     * Used to ensure compatibility when loading saved models.
     * Format: MAJOR.MINOR.PATCH
     */
    public static final String fileVersion = "1.0.0";

    /**
     * Header string for Lyra model files.
     * Used to identify and validate Lyra model files during loading.
     * The complete header includes the fileVersion appended to this string.
     */
    public static final String lyraFileHeader = "LyraNeural2 by Equinox Electronic v";
}


//This is a general settings class. As you can see, there
//are not that many values in, but I hope that will change soon.

//Equinox Electronic