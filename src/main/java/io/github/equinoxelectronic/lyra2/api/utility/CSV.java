package io.github.equinoxelectronic.lyra2.api.utility;

import io.github.equinoxelectronic.lyra2.objects.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {

    public static DataSet readTrainingData(String filepath, String delimiter) {
        ArrayList<ArrayList<Double>> inputs = new ArrayList<>();
        ArrayList<ArrayList<Double>> outputs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip header if exists
                if (isHeader) {
                    isHeader = false;
                    if (line.toLowerCase().contains("input") || line.toLowerCase().contains("output")) {
                        continue;
                    }
                }

                // Split into input and output sections
                String[] parts = line.split("~");
                if (parts.length != 2) {
                    continue; // Skip malformed lines
                }

                // Process input values
                ArrayList<Double> inputRow = new ArrayList<>();
                for (String val : parts[0].split(delimiter)) {
                    if (!val.trim().isEmpty()) {
                        try {
                            inputRow.add(Double.parseDouble(val.trim()));
                        } catch (NumberFormatException e) {
                            // Skip non-numeric values
                            continue;
                        }
                    }
                }

                // Process output values
                ArrayList<Double> outputRow = new ArrayList<>();
                for (String val : parts[1].split(delimiter)) {
                    if (!val.trim().isEmpty()) {
                        try {
                            outputRow.add(Double.parseDouble(val.trim()));
                        } catch (NumberFormatException e) {
                            // Skip non-numeric values
                            continue;
                        }
                    }
                }

                // Only add if both input and output have values
                if (!inputRow.isEmpty() && !outputRow.isEmpty()) {
                    inputs.add(inputRow);
                    outputs.add(outputRow);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage());
        }

        return new DataSet(inputs, outputs);
    }
}

//This is a simple class that converts serialized CSV data into a DataSet that the model can read.

//Equinox Electronic
