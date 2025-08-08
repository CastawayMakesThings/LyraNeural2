package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.Versioning;
import com.google.gson.Gson;
import io.github.equinoxelectronic.equinox_essentials.Essentials;
import org.tukaani.xz.XZInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

/**
 * Provides functionality for loading neural network models from files.
 * Supports both JSON and compressed binary formats with version compatibility checking.
 */
public class Loading {

    /**
     * Loads a model from a JSON-formatted .lyra file.
     * This method is provided for legacy support and debugging purposes.
     * For production use, prefer {@link #loadModel(String)} which uses a more
     * efficient binary format.
     *
     * @param filepath Path to the model file (with or without .lyra extension)
     * @return The loaded LyraModel instance
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public static LyraModel loadJSONModel(String filepath) {
        if(!(filepath.endsWith(".lyra"))) {
            filepath = filepath + ".lyra";
        }

        LyraModel model;
        try {
            Gson gson = new Gson();
            model = gson.fromJson(new FileReader(filepath), LyraModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model from " + filepath, e);
        }

        if(model.lyraVersion != Config.version) {
            model = Versioning.updateModel(model);
        }

        return model;
    }

    /**
     * Loads a model from a compressed binary .lyra file.
     * This is the preferred method for loading models as it provides better performance
     * and smaller file sizes compared to JSON format.
     *
     * The loading process includes:
     * 1. Reading and validating the compressed file content
     * 2. Decompressing the content
     * 3. Parsing the model structure
     * 4. Version compatibility checking and updating if necessary
     *
     * @param filepath Path to the model file (with or without .lyra extension)
     * @return The loaded LyraModel instance
     * @throws RuntimeException if the file cannot be read, decompressed, or parsed
     */
    public static LyraModel loadModel(String filepath) {
        if (!(filepath.endsWith(".lyra"))) {
            filepath = filepath + ".lyra";
        }

        try (Scanner reader = new Scanner(new FileReader(filepath))) {
            if (!reader.hasNextLine()) {
                throw new IOException("Empty model file: " + filepath);
            }
            String fileContent = reader.nextLine();
            if (fileContent == null || fileContent.isEmpty()) {
                throw new IOException("Invalid model content in: " + filepath);
            }

            String decompressed = decompressString(fileContent);
            if (decompressed == null || decompressed.isEmpty()) {
                throw new IOException("Decompression failed for: " + filepath);
            }

            LyraModel model = Parser.parseModelFile(decompressed);
            if (!Objects.equals(model.lyraVersion, Config.fileVersion)) {
                model = Versioning.updateModel(model);
            }

            Essentials.logger.logString("Loaded model " + model.modelID + " by " + model.modelAuthor + " successfully!");
            return model;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model from " + filepath, e);
        }
    }

    /**
     * Decompresses a Base64-encoded XZ-compressed string.
     * Uses a 4KB buffer for efficient memory usage during decompression.
     *
     * @param compressedBase64 The Base64-encoded compressed string
     * @return The decompressed string in UTF-8 encoding
     * @throws IOException if decompression fails
     */
    public static String decompressString(String compressedBase64) throws IOException {
        byte[] compressedData = Base64.getDecoder().decode(compressedBase64);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             XZInputStream xzIn = new XZInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int n;
            while ((n = xzIn.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toString(StandardCharsets.UTF_8);
        }
    }
}


//This is a very basic class that loads up a saved model file. As you can see,
//it uses JSON. This is definetly suboptimal for larger models, so I intend to write
//my parser.

//Equinox Electronic
