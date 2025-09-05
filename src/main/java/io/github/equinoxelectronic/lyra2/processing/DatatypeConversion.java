package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;

import java.util.ArrayList;

/**
 * Handles conversion between standard Java datatypes and binary representations for neural network processing.
 * This utility class enables the neural network to work with various input/output types by converting them
 * to and from binary (0/1) arrays that the network can process.
 *
 * Supported Types:
 * - RAW: Direct ArrayList&lt;Double&gt; input/output
 * - INTEGER: 32-bit integer (int)
 * - FLOAT: 32-bit floating point
 * - DOUBLE: 64-bit floating point
 * - LONG: 64-bit integer
 * - CHAR: 8-bit character
 * - BYTE: 8-bit integer (byte)
 * - SHORT: 16-bit integer (short)
 * - BOOLEAN: 1-bit boolean
 */
public class DatatypeConversion {

    /**
     * Converts a value to its binary representation as an ArrayList of Doubles.
     * Each element in the resulting array is either 0.0 or 1.0, representing
     * individual bits of the input value.
     *
     * Bit lengths for different types:
     * - INTEGER/FLOAT: 32 bits
     * - DOUBLE/LONG: 64 bits
     * - CHAR/BYTE: 8 bits
     * - SHORT: 16 bits
     * - BOOLEAN: 1 bit
     * - RAW: Passed through as-is
     *
     * @param inputType The type of input being converted
     * @param input The value to convert
     * @return ArrayList of Doubles representing the binary form
     * @throws LyraWrongDatatypeException if input type is invalid or input is null
     */
    public static ArrayList<Double> convertToBinaryArray(Enums.IOType inputType, Object input)
            throws LyraWrongDatatypeException {
        if (!isValidDataType(inputType, input)) {
            throw new LyraWrongDatatypeException("Invalid input type or null input");
        }

        ArrayList<Double> binaryList = new ArrayList<>();

        return switch (inputType) {
            case RAW -> new ArrayList<>((ArrayList<Double>) input);
            case INTEGER -> {
                int value = (Integer) input;
                for (int i = 31; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                yield binaryList;
            }
            case FLOAT -> {
                int bits = Float.floatToIntBits((Float) input);
                for (int i = 31; i >= 0; i--) {
                    binaryList.add((double) ((bits >>> i) & 1));
                }
                yield binaryList;
            }
            case DOUBLE -> {
                long bits = Double.doubleToLongBits((Double) input);
                for (int i = 63; i >= 0; i--) {
                    binaryList.add((double) ((bits >>> i) & 1));
                }
                yield binaryList;
            }
            case LONG -> {
                long value = (Long) input;
                for (int i = 63; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                yield binaryList;
            }
            case CHAR -> {
                char value = (Character) input;
                for (int i = 7; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                yield binaryList;
            }
            case BYTE -> {
                byte value = (Byte) input;
                for (int i = 7; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                yield binaryList;
            }
            case SHORT -> {
                short value = (Short) input;
                for (int i = 15; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                yield binaryList;
            }
            case BOOLEAN -> {
                boolean value = (Boolean) input;
                binaryList.add(value ? 1.0 : 0.0);
                yield binaryList;
            }
            default -> throw new LyraWrongDatatypeException("Unsupported type");
        };
    }

    /**
     * Converts a binary array back to its original datatype.
     * This is the inverse operation of convertToBinaryArray.
     *
     * @param outputType The desired output type
     * @param binaryArray The binary representation to convert
     * @return Object of the specified output type
     * @throws LyraWrongDatatypeException if binary array is null or has wrong size
     */
    public static Object convertFromBinaryArray(Enums.IOType outputType, ArrayList<Double> binaryArray)
            throws LyraWrongDatatypeException {
        if (binaryArray == null) throw new LyraWrongDatatypeException("Binary array is null");

        // Round values to ensure binary (0/1) values
        for (int i = 0; i < Math.min(32, binaryArray.size()); i++) {
            binaryArray.set(i, (double) Math.round(binaryArray.get(i)));
        }

        return switch (outputType) {
            case RAW -> new ArrayList<>(binaryArray);
            case INTEGER -> bitsToInt(binaryArray, 32);
            case FLOAT -> Float.intBitsToFloat(bitsToInt(binaryArray, 32));
            case DOUBLE -> Double.longBitsToDouble(bitsToLong(binaryArray, 64));
            case LONG -> bitsToLong(binaryArray, 64);
            case CHAR -> (char) bitsToInt(binaryArray, 8);
            case BYTE -> (byte) bitsToInt(binaryArray, 8);
            case SHORT -> (short) bitsToInt(binaryArray, 16);
            case BOOLEAN -> {
                if (binaryArray.size() != 1) {
                    throw new LyraWrongDatatypeException("Invalid bit size for BOOLEAN: " + binaryArray.size());
                }
                yield binaryArray.get(0).intValue() == 1;
            }
            default -> throw new LyraWrongDatatypeException("Unsupported output type");
        };
    }

    /**
     * Returns the number of bits required to represent a given type.
     *
     * @param type The IOType to check
     * @return Number of bits needed (-1 for RAW type)
     * @throws LyraWrongDatatypeException if type is unknown
     */
    public static int getBitCount(Enums.IOType type) throws LyraWrongDatatypeException {
        return switch (type) {
            case RAW -> -1;
            case INTEGER, FLOAT -> 32;
            case DOUBLE, LONG -> 64;
            case CHAR, BYTE -> 8;
            case SHORT -> 16;
            case BOOLEAN -> 1;
            default -> throw new LyraWrongDatatypeException("Unknown type");
        };
    }

    /**
     * Validates that an input object matches the specified IOType.
     *
     * @param type Expected IOType
     * @param input Object to validate
     * @return true if input is valid for the specified type
     */
    public static boolean isValidDataType(Enums.IOType type, Object input) {
        if (input == null) return false;

        return switch (type) {
            case RAW -> input instanceof ArrayList<?> &&
                    ((ArrayList<?>) input).stream().allMatch(el -> el instanceof Double);
            case INTEGER -> input instanceof Integer;
            case FLOAT -> input instanceof Float;
            case DOUBLE -> input instanceof Double;
            case LONG -> input instanceof Long;
            case CHAR -> input instanceof Character;
            case BYTE -> input instanceof Byte;
            case SHORT -> input instanceof Short;
            case BOOLEAN -> input instanceof Boolean;
        };
    }

    /**
     * Converts a binary array to an integer value.
     *
     * @param bits Binary representation
     * @param expectedSize Expected number of bits
     * @return Integer value
     * @throws LyraWrongDatatypeException if array size doesn't match expected size
     */
    private static int bitsToInt(ArrayList<Double> bits, int expectedSize) throws LyraWrongDatatypeException {
        if (bits.size() != expectedSize) {
            throw new LyraWrongDatatypeException("Invalid bit size for INT: " + bits.size());
        }
        int value = 0;
        for (int i = 0; i < expectedSize; i++) {
            value = (value << 1) | bits.get(i).intValue();
        }
        return value;
    }

    /**
     * Converts a binary array to a long value.
     *
     * @param bits Binary representation
     * @param expectedSize Expected number of bits
     * @return Long value
     * @throws LyraWrongDatatypeException if array size doesn't match expected size
     */
    private static long bitsToLong(ArrayList<Double> bits, int expectedSize) throws LyraWrongDatatypeException {
        if (bits.size() != expectedSize) {
            throw new LyraWrongDatatypeException("Invalid bit size for LONG/DOUBLE: " + bits.size());
        }
        long value = 0;
        for (int i = 0; i < expectedSize; i++) {
            value = (value << 1) | bits.get(i).intValue();
        }
        return value;
    }
}



//==========================================================================================
//== Since model's inputs are an array of values between 0 and 1, we can make the model   ==
//== take in different datatypes (int, char, long, etc.) by converting the wanted input   ==
//== value and converting it into a binary array, which is what model take in. This class ==
//== does just that and the opposite, converting a binary array into a normal value.      ==
//== In the future I intend to add more datatypes, maybe even Strings, but I have left    ==
//== only bare minimum datatypes. For now, if you wanted to train a model that took in or ==
//== out Strings, then you would need to choose the RAW datatype and convert it yourself. ==
//==========================================================================================

//Equinox Electronic