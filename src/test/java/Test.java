import com.equinox.lyra2.Enums;
import com.equinox.lyra2.api.Running;
import com.equinox.lyra2.api.Trainer;
import com.equinox.lyra2.objects.LyraModel;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {

//TODO |        Found the issue! It has to do with datatype conversion. There seems to be no issue with the training.

        LyraModel model = new LyraModel();
        model = model.builder().name("testModel")
                .author("EquinoxElectronic")
                .inputType(Enums.IOType.INTEGER)
                .outputType(Enums.IOType.INTEGER)
                .setActivationFunction(Enums.activationFunctions.TANH)
                .addHiddenLayer(10)
                .addHiddenLayer(10)
                .addHiddenLayer(10)
                .build();

        ArrayList<Object> idata = new ArrayList<>();
        idata.add(1);
        idata.add(2);
        idata.add(3);
        idata.add(4);
        idata.add(5);
        idata.add(6);
        idata.add(7);
        idata.add(8);
        idata.add(9);

        ArrayList<Object> odata = new ArrayList<>();
        odata.add(3);
        odata.add(4);
        odata.add(5);
        odata.add(6);
        odata.add(7);
        odata.add(8);
        odata.add(9);
        odata.add(10);
        odata.add(11);

        Trainer trainer = new Trainer();
        trainer.configure()
                .setModel(model)
                .setInputData(idata)
                .setOutputData(odata)
                .setErrorThreshold(0.1)
                .setLearningRate(0.002)
                .setStatusPrintInterval(10000)
                .setTimeLimit(60);

        model = trainer.train();

        System.out.println(Running.feed(model, 3));
    }
}