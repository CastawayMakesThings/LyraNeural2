# LyraNeural 2
## About LyraNeural 2
LyraNeural 2 is a lightweight neural network library for Java, designed with simplicity and ease of use in mind. It provides a flexible architecture for creating, training, and deploying neural networks with features including:

- Simple and intuitive model building using a builder pattern
- Support for multiple activation functions (ReLU, Leaky ReLU, TanH)
- Efficient model serialization and loading
- Parallel processing for improved training performance
- Built-in data type conversion handling
- Customizable network architectures

Perfect for those looking to implement neural networks in Java without the complexity of larger frameworks.

## Quickstart
> [!NOTE]
> This is not a guide on how to use AI, you should already know tha basics of machine learning to be able to use Lyra2. 3Blue1Brown has a great tutorial on YouTube.
> The first step of using Lyra2 is to create a `LyraModel` object. You can create this simply by using

```java
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

LyraModel model = new LyraModel();
```
For now, let's assume you want to train a new model. To initialize this model for training, use the `builder()` method in the 
model object. Here is an example of a simple model:

```java
import io.github.equinoxelectronic.lyra2.Enums;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

class Example {
    public static void main(String[] args) {
        LyraModel model = new LyraModel();
        model.builder()
                .name("Test Model")
                .author("AnotherCastaway")
                .inputType(Enums.IOType.INTEGER)
                .outputType(Enums.IOType.RAW)
                .setActivationFunction(Enums.activationFunctions.RELU)
                .addHiddenLayer(40)
                .addHiddenLayer(60)
                .addHiddenLayer(20)
                .backLayerSize(10) //You can only set the size of the front/back layer if it's datatype is RAW
                .metadata("Some metadata")
                .build();
    }
}
```

This creates a small model, but it can not be used for anything useful. You would need to `train` the model. To train it, you would need some
data. The default datatype for training data is two `ArrayList<Object>`. The first is the inputs, the second is the wanted results for that input.
The kind of object in the ArrayList is the datatype you set for the input or output. Each object of the `ArrayList` represents one example, so their
needs to be an equal amount of objects in the first `ArrayList` as their are in the second. Also note that you can use primitive arrays, and our
very own `TrainingData` object that contains both of the `ArrayLists`, and you can load that from a CSV. 
Once you have the data ready, configure a `Trainer` object as shown below.

```java
    public class Main {
    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.configure()
                .setModel(model) //The model to train
                .setInputData(idata) //The input data
                .setEpochLimit(30000) //The amount of epochs (iterations) it is allowed to train.
                .setOutputData(odata) //The output data
                .setErrorThreshold(0.01) //The error threshold. When it reaches this, training will stop.
                .setLearningRate(0.002) //The learning rate.
                .setPrimaryTrainingStopper(Enums.trainingStoppers.EPOCH) //This does not affect the model, but simply decides which unit to print on loading bar
                .setStatusPrintingMethod("bar") //Tells it display status as a loading bar, rather then occasional updates
                .setTimeLimit(60); //The time limit in seconds.

    }
}
```
This will take up some 20 seconds depending on your computer's specs. Once a model is trained, you can save it.
Models are saved as Lyrafiles, a custom-built file format that is still under development. To save it, simple use the
`save()` method as shown below.

```java
public class Main {
    public static void main(String[] args) {
        LyraModel model = new LyraModel();
        
        //Model initialization and training...
        
        model.save("path/to/where/you/want/to/save/it"); 
    }
}
```

To load a model, create a new LyraModel, and use the `load()` method.

```java
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

public class Main {
    public static void main(String[] args) {
        LyraModel loadedModel = new LyraModel();
        loadedModel.load("path/to/saved/model");
    }
}
```

There are some other useful utility tools such as CSV loading, data normalization, data splitting, metrics calculation,
model visualizing, and validation tools.