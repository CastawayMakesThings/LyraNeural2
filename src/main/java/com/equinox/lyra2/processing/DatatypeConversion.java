package com.equinox.lyra2.processing;

import com.equinox.lyra2.Enums;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;

import java.util.ArrayList;

public class DatatypeConversion {

    //Converts the object into a model-readable arraylist
    public static ArrayList<Double> convertToBinaryArray(Enums.IOType inputType, Object input) throws LyraWrongDatatypeException {
        switch (inputType) {
            case RAW:
                try {
                    return (ArrayList<Double>) input;
                } catch (Exception e) {
                    throw new LyraWrongDatatypeException();
                }
            case INTEGER:
                int[] bits = intToBits((Integer) input);
                ArrayList<Double> result = new ArrayList<>();
                for (int bit : bits) {
                    result.add((double) bit);
                }
                try {
                    return result;
                } catch (Exception e) {
                    throw new LyraWrongDatatypeException();
                }
            case DOUBLE:
                try {
                    double value = (Double) input;
                    ArrayList<Double> doubleResult = new ArrayList<>();
                    long bits64 = Double.doubleToLongBits(value);
                    for (int i = 63; i >= 0; i--) {
                        doubleResult.add((double) ((bits64 >>> i) & 1));
                    }
                    return doubleResult;
                } catch (Exception e) {
                    throw new LyraWrongDatatypeException();
                }
            case STRING:
                try {
                    String str = (String) input;
                    ArrayList<Double> stringResult = new ArrayList<>();
                    for (char c : str.toCharArray()) {
                        for (int i = 15; i >= 0; i--) {
                            stringResult.add((double) ((c >>> i) & 1));
                        }
                    }
                    return stringResult;
                } catch (Exception e) {
                    throw new LyraWrongDatatypeException();
                }
            case LONG:
                try {
                    long value = (Long) input;
                    ArrayList<Double> longResult = new ArrayList<>();
                    for (int i = 63; i >= 0; i--) {
                        longResult.add((double) ((value >>> i) & 1));
                    }
                    return longResult;
                } catch (Exception e) {
                    throw new LyraWrongDatatypeException();
                }
            default:
                throw new LyraWrongDatatypeException();
        }
    }
    
    

    //Helper to convert binary to ints, and ints to binary
    public static int[] intToBits(int n) {
        int[] bits = new int[32];
        for (int i = 31; i >= 0; i--) {
            bits[31 - i] = (n >>> i) & 1;
        }
        return bits;
    }
    public static int bitsToInt(int[] bits) {
        int n = 0;
        for (int i = 0; i < 32; i++) {
            n <<= 1; // shift left
            n |= bits[i]; // OR in the next bit (must be 0 or 1)
        }
        return n;
    }

    public static Object convertFromBinaryArray(Enums.IOType outputType, ArrayList<Double> binaryArray) throws LyraWrongDatatypeException {
        if (binaryArray == null) {
            throw new LyraWrongDatatypeException();
        }

        switch (outputType) {
            case RAW:
                return binaryArray;
            case INTEGER:
                if (binaryArray.size() != 32) {
                    throw new LyraWrongDatatypeException();
                }
                int[] bits = new int[32];
                for (int i = 0; i < 32; i++) {
                    bits[i] = binaryArray.get(i).intValue();
                }
                return bitsToInt(bits);
            case DOUBLE:
                if (binaryArray.size() != 64) {
                    throw new LyraWrongDatatypeException();
                }
                long doubleBits = 0L;
                for (int i = 0; i < 64; i++) {
                    doubleBits = (doubleBits << 1) | binaryArray.get(i).intValue();
                }
                return Double.longBitsToDouble(doubleBits);
            case STRING:
                if (binaryArray.size() % 16 != 0) {
                    throw new LyraWrongDatatypeException();
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < binaryArray.size(); i += 16) {
                    char c = 0;
                    for (int j = 0; j < 16; j++) {
                        c = (char) ((c << 1) | binaryArray.get(i + j).intValue());
                    }
                    str.append(c);
                }
                return str.toString();
            case LONG:
                if (binaryArray.size() != 64) {
                    throw new LyraWrongDatatypeException();
                }
                long value = 0L;
                for (int i = 0; i < 64; i++) {
                    value = (value << 1) | binaryArray.get(i).intValue();
                }
                return value;
            default:
                throw new LyraWrongDatatypeException();
        }
    }
}
