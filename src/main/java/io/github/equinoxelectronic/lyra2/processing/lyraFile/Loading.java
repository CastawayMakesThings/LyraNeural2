package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;
import io.github.equinoxelectronic.lyra2.processing.Versioning;
import com.google.gson.Gson;
import io.github.equinoxelectronic.equinox_essentials.Essentials;
import org.tukaani.xz.XZInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class Loading {
    public static LyraModel loadJSONModel(String filepath) {
        //Checks if filepath is valid
        if(!(filepath.endsWith(".lyra"))) {
            filepath = filepath+".lyra";
        }
        
        //Attempts to deserialize the file.
        LyraModel model;
        try {
            Gson gson = new Gson();
            model = gson.fromJson(new FileReader(filepath), LyraModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model from " + filepath, e);
        }

        //Checks the lyra file
        if(model.lyraVersion != Config.version) {
            model = Versioning.updateModel(model);
        }

        return model;
    }

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
