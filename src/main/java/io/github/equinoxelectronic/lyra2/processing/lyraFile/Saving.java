package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.ModelChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.equinoxelectronic.equinox_essentials.Essentials;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Provides functionality for persisting neural network models to files.
 * Supports both JSON and compressed binary formats with built-in path handling
 * and model validation.
 */
public class Saving {

    /**
     * Saves a model to a JSON-formatted .lyra file.
     * This method is primarily for debugging and development purposes.
     * For production use, prefer {@link #saveModel} which uses a more
     * efficient binary format.
     *
     * @param filepath Path where the model should be saved
     * @param model The model to save
     * @throws RuntimeException if the model cannot be saved
     */
    public static void saveModelAsJSON(String filepath, LyraModel model) {
        ModelChecker.checkModel(model);

        filepath = normalizeFilepath(filepath, model);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(model, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model to " + filepath, e);
        }
    }

    /**
     * Saves a model to a compressed binary .lyra file.
     * This is the preferred method for saving models as it provides better performance
     * and smaller file sizes compared to JSON format.
     *
     * The saving process includes:
     * 1. Model validation
     * 2. Path normalization
     * 3. Model serialization
     * 4. Data compression
     * 5. File writing
     *
     * @param filepath Path where the model should be saved
     * @param model The model to save
     * @throws RuntimeException if the model cannot be saved or compressed
     */
    public static void saveModel(String filepath, LyraModel model) {
        ModelChecker.checkModel(model);

        filepath = normalizeFilepath(filepath, model);

        String serializedModel = Serializer.serializeModel(model);

        try {
            serializedModel = compressString(serializedModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress model data", e);
        }

        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(serializedModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model to " + filepath, e);
        }
        Essentials.logger.logString("Saved model to " + filepath + " successfully!");
    }

    /**
     * Compresses a string using LZMA2 compression and encodes it in Base64.
     * Uses maximum compression level for optimal file size reduction.
     *
     * @param input String to compress
     * @return Base64-encoded compressed string
     * @throws IOException if compression fails
     */
    public static String compressString(String input) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            LZMA2Options options = new LZMA2Options(LZMA2Options.PRESET_MAX);
            try (XZOutputStream xzOut = new XZOutputStream(baos, options)) {
                xzOut.write(input.getBytes(StandardCharsets.UTF_8));
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    /**
     * Normalizes the filepath by ensuring it has the correct extension
     * and handling directory paths appropriately.
     *
     * @param filepath Original filepath
     * @param model Model being saved (used for filename if needed)
     * @return Normalized filepath
     */
    private static String normalizeFilepath(String filepath, LyraModel model) {
        filepath = filepath.trim();
        if (filepath.endsWith("/") || filepath.endsWith("\\")) {
            filepath = filepath + model.modelID + ".lyra";
        }
        if (!filepath.endsWith(".lyra")) {
            filepath = filepath + ".lyra";
        }
        return filepath;
    }
}



//A very simple class that saves a model into a JSON file. As mentioned in the comments
//for Loading.java, this is less than ideal for large models, so I do intend to write my
//own serializer to save models.

//Equinox Electronic