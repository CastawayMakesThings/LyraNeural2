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
The first step of using Lyra2 is to create a `LyraModel` object. You can create this simply by using

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

This creates a small model