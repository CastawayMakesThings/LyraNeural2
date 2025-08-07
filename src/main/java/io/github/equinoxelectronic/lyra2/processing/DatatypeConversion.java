package io.github.equinoxelectronic.lyra2.processing;

import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.exceptions.LyraWrongDatatypeException;

import java.util.ArrayList;

public class DatatypeConversion {

    //This methods converts a single value into an ArrayList of it's binary value.
    //E.g 12 -> {0,0,0,0,1,1,0,0}
    public static ArrayList<Double> convertToBinaryArray(Enums.IOType inputType, Object input) throws LyraWrongDatatypeException {
        //Checks to make sure inputted datatype is valid
        if (!isValidDataType(inputType, input)) {
            throw new LyraWrongDatatypeException("Invalid input type or null input");
        }

        //The output ArrayList
        ArrayList<Double> binaryList = new ArrayList<>();

        //Calculates the values depending in the datatype
        switch (inputType) {
            case RAW:
                return new ArrayList<>((ArrayList<Double>) input); // shallow copy for safety

            case INTEGER: {
                int value = (Integer) input;
                for (int i = 31; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                return binaryList;
            }

            case FLOAT: {
                int bits = Float.floatToIntBits((Float) input);
                for (int i = 31; i >= 0; i--) {
                    binaryList.add((double) ((bits >>> i) & 1));
                }
                return binaryList;
            }

            case DOUBLE: {
                long bits = Double.doubleToLongBits((Double) input);
                for (int i = 63; i >= 0; i--) {
                    binaryList.add((double) ((bits >>> i) & 1));
                }
                return binaryList;
            }

            case LONG: {
                long value = (Long) input;
                for (int i = 63; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                return binaryList;
            }

            case CHAR: {
                char value = (Character) input;
                for (int i = 7; i >= 0; i--) {
                    binaryList.add((double) ((value >>> i) & 1));
                }
                return binaryList;
            }

            default:
                throw new LyraWrongDatatypeException("Unsupported type");
        }
    }

    //This does the opposite of the previous method. It converts a binary ArrayList
    //into it's normal value.
    //E.g {0,0,0,0,1,1,0,0} -> 12
    public static Object convertFromBinaryArray(Enums.IOType outputType, ArrayList<Double> binaryArray) throws LyraWrongDatatypeException {
        if (binaryArray == null) throw new LyraWrongDatatypeException("Binary array is null");

        //Round the binary values to 1 or 0
        for (int i = 0; i < 32; i++) {
            binaryArray.set(i, (double) Math.round(binaryArray.get(i)));
        }

        //Calculates the output depending on the datatype
        switch (outputType) {
            case RAW:
                return new ArrayList<>(binaryArray);

            case INTEGER:
                return bitsToInt(binaryArray, 32);

            case FLOAT:
                int floatBits = bitsToInt(binaryArray, 32);
                return Float.intBitsToFloat(floatBits);

            case DOUBLE:
                long doubleBits = bitsToLong(binaryArray, 64);
                return Double.longBitsToDouble(doubleBits);

            case LONG:
                return bitsToLong(binaryArray, 64);

            case CHAR:
                return (char) bitsToInt(binaryArray, 8);

            default:
                throw new LyraWrongDatatypeException("Unsupported output type");
        }
    }

    //This is a helper method that gets how many bits are needed for a datatype
    public static int getBitCount(Enums.IOType type) throws LyraWrongDatatypeException {
        return switch (type) {
            case RAW -> -1;
            case INTEGER, FLOAT -> 32;
            case DOUBLE, LONG -> 64;
            case CHAR -> 8;
            default -> throw new LyraWrongDatatypeException("Unknown type");
        };
    }

    //Another helper method that confirms if an object is of the right datatype
    public static boolean isValidDataType(Enums.IOType type, Object input) {
        if (input == null) return false;

        return switch (type) {
            case RAW -> input instanceof ArrayList<?> && ((ArrayList<?>) input).stream().allMatch(el -> el instanceof Double);
            case INTEGER -> input instanceof Integer;
            case FLOAT -> input instanceof Float;
            case DOUBLE -> input instanceof Double;
            case LONG -> input instanceof Long;
            case CHAR -> input instanceof Character;
        };
    }

    //Converts an arraylist of bits to an integer
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

    //Converts bits to a long.
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