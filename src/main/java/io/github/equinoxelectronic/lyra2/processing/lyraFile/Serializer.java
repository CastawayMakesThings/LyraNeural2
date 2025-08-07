package io.github.equinoxelectronic.lyra2.processing.lyraFile;

import io.github.equinoxelectronic.lyra2.Config;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

public class Serializer {
    public static String serializeModel(LyraModel model) {
        //The string that will be constructor
        String s;

        //=============================================================
        // All elements will be seperated by ␞. To prevent this charecter from being
        // used in metadata, TODO the program will check for it when the model is built.
        //=============================================================

        //Sets the header
        s = Config.lyraFileHeader + Config.fileVersion + "␞";

        //Sets the metadata
        s += model.modelID + "␞"+model.modelAuthor+"␞"+model.metadata+"␞";

        //Sets the version
        s += model.lyraVersion + "␞";

        //Sets the output and input type
        s += model.frontLayer.inputType.name() + "␞" + model.outputType.name() + "␞";

        //Sets the model's activation function
        s += model.activationFunction.name() + "␞";

        //Sets the front layer.
        s += model.frontLayer.neurons.size()  + "␞";

        //Starts with the hidden layers. Layers seperated by slashes, neurons seperated
        // by semicolons and weights seperated by commas. The bias is seperated by a ^.

        //For every layer
        for(int i = 0; i < model.layers.size(); i++) {
            //For every neuron in that layer
            for (int j = 0; j < model.layers.get(i).neurons.size(); j++) {
                s += model.layers.get(i).neurons.get(j).bias + "^";
                //For every weight in that neuron in that layer
                for (int k = 0; k < model.layers.get(i).neurons.get(j).weights.size(); k++) {
                    s += model.layers.get(i).neurons.get(j).weights.get(k);
                    if (k < model.layers.get(i).neurons.get(j).weights.size() - 1) {
                        s += ",";
                    }
                }
                s+= ";";
            }
            if (i < model.layers.size() - 1) {
                s += "/";
            }
        }

        return s;
    }
}

//This is a simple serializer that takes in a model and spits it out as a parseable string that will be stored
//in a lyrafile.

//Equinox Electronic