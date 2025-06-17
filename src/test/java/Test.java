import com.equinox.lyra2.Config;
import com.equinox.lyra2.Enums;
import com.equinox.lyra2.errors.LyraWrongDatatypeException;
import com.equinox.lyra2.pojo.LyraModel;
import com.equinox.lyra2.processing.Feeding;
import com.equinox.lyra2.processing.Initialization;
import com.equinox.lyra2.processing.Training;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        LyraModel model = new LyraModel();
        Config.VERBOSE = false;
        ArrayList<Integer> neuronsPerLayer = new ArrayList<>();
        neuronsPerLayer.add(2);    // Input layer
        neuronsPerLayer.add(4);    // Bigger first hidden layer
        neuronsPerLayer.add(3);    // Second hidden layer
        neuronsPerLayer.add(2);    // Output layer


        ArrayList<Enums.activationFunctions> activationFunctions = new ArrayList<>();
        activationFunctions.add(Enums.activationFunctions.TANH);
        activationFunctions.add(Enums.activationFunctions.LEAKY_RELU);  // LeakyReLU prevents dying
        activationFunctions.add(Enums.activationFunctions.LEAKY_RELU);  // LeakyReLU for second hidden
        activationFunctions.add(Enums.activationFunctions.TANH);



        Initialization.initializeModel(model, "testModel", "EquinoxElectronic", Enums.IOType.RAW, Enums.IOType.RAW, neuronsPerLayer, activationFunctions);

        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        ArrayList<Double> data1 = new ArrayList<>();
        data1.add(1.0);
        data1.add(0.0);
        data.add(data1);
        ArrayList<Double> data2 = new ArrayList<>();
        data2.add(0.0);
        data2.add(1.0);
        data.add(data2);
        ArrayList<Double> data3 = new ArrayList<>();
        data3.add(1.0);
        data3.add(1.0);
        data.add(data3);
        ArrayList<Double> data4 = new ArrayList<>();
        data4.add(0.0);
        data4.add(0.0);
        data.add(data4);

        ArrayList<ArrayList<Double>> outputData = new ArrayList<>();
        ArrayList<Double> outputData1 = new ArrayList<>();
        outputData1.add(1.0);
        outputData1.add(-1.0);
        outputData.add(outputData1);
        ArrayList<Double> outputData2 = new ArrayList<>();
        outputData2.add(-1.0);
        outputData2.add(1.0);
        outputData.add(outputData2);
        ArrayList<Double> outputData3 = new ArrayList<>();
        outputData3.add(1.0);
        outputData3.add(1.0);
        outputData.add(outputData3);
        ArrayList<Double> outputData4 = new ArrayList<>();
        outputData4.add(-1.0);
        outputData4.add(-1.0);
        outputData.add(outputData4);

        Training.trainModel(model, data, outputData, 200000, 0.01);

        ArrayList<Double> output1 = null;
        try {
            output1 = Feeding.feedForward(model, data1); //Should print 1,0
        } catch (LyraWrongDatatypeException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < output1.size(); i++) {
            System.out.println(output1.get(i));
        }

    }
}
