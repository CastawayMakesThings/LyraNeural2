package com.equinox.lyra2.processing;

import com.equinox.lyra2.Enums;
import com.equinox.lyra2.exceptions.LyraWrongDatatypeException;

import java.util.ArrayList;

public class DatatypeConversion {

    public static ArrayList<Double> convertToBinaryArray(Enums.IOType inputType, Object input) throws LyraWrongDatatypeException {
        if (!isValidDataType(inputType, input)) {
            throw new LyraWrongDatatypeException("Invalid input type or null input");
        }

        ArrayList<Double> binaryList = new ArrayList<>();

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

    public static Object convertFromBinaryArray(Enums.IOType outputType, ArrayList<Double> binaryArray) throws LyraWrongDatatypeException {
        if (binaryArray == null) throw new LyraWrongDatatypeException("Binary array is null");

        for (int i = 0; i < 32; i++) {
            binaryArray.set(i, (double) Math.round(binaryArray.get(i)));
        }

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

    public static int getBitCount(Enums.IOType type) throws LyraWrongDatatypeException {
        return switch (type) {
            case RAW -> -1;
            case INTEGER, FLOAT -> 32;
            case DOUBLE, LONG -> 64;
            case CHAR -> 8;
            default -> throw new LyraWrongDatatypeException("Unknown type");
        };
    }

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
