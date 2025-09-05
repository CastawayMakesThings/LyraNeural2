# LyraNeural 2

A lightweight neural network library for Java focused on simplicity, readable APIs, and fast iteration.

- Project status: Active development
- Minimum Java: 21
- Build tool: Maven

## Table of Contents
- [About LyraNeural 2](#about-lyraneural-2)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
  - [Use via JitPack](#use-via-jitpack)
  - [Build from source](#build-from-source)
- [Quickstart](#quickstart)
- [Documentation](#documentation)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## About LyraNeural 2
LyraNeural 2 is a lightweight neural network library for Java, designed with simplicity and ease of use in mind. It provides a flexible architecture for creating, training, and deploying neural networks with features including:

## Features
- Simple and intuitive model building using a builder pattern
- Support for multiple activation functions (ReLU, Leaky ReLU, TanH)
- Efficient model serialization and loading
- Parallel processing for improved training performance
- Built-in data type conversion handling
- Customizable network architectures

Perfect for those looking to implement neural networks in Java without the complexity of larger frameworks.

## Requirements
- Java 21 or newer
- Maven 3.9+ (if building from source)

## Installation
You can use LyraNeural 2 via JitPack or build it locally from source.

### Use via JitPack
1. Add JitPack to your repositories:
   
   Maven settings (in your project's pom.xml):
   
   ```xml
   <repositories>
     <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
     </repository>
   </repositories>
   ```
2. Add the dependency (replace the version with the latest tag as needed):
   
   ```xml
   <dependency>
     <groupId>com.github.CastawayMakesThings</groupId>
     <artifactId>Lyra2</artifactId>
     <version>v1.0.1</version>
   </dependency>
   ```

### Build from source
- Clone the repository and build:
  
  ```bash
  mvn -q -DskipTests package
  ```
  
  The JAR will be available under `target/`.

## Quickstart
> [!NOTE]
> This is not a guide on how to use AI; you should already know the basics of machine learning to be able to use Lyra2. 3Blue1Brown has a great tutorial on YouTube.
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
                .backLayerSize(10) // You can only set the size of the front/back layer if its datatype is RAW
                .metadata("Some metadata")
                .build();
    }
}
```

This creates a small model, but it cannot be used for anything useful yet. You need to train the model. The default datatype for training data is two `ArrayList<Object>` instances: the first is the inputs, the second is the expected results for that input. The kind of object in the `ArrayList` is the datatype you set for the input or output. Each object of the `ArrayList` represents one example, so there needs to be an equal number of objects in the first `ArrayList` as there are in the second. Also note that you can use primitive arrays, and our very own `TrainingData` object that contains both of the `ArrayLists`, and you can load that from a CSV.
Once you have the data ready, configure a `Trainer` object as shown below.

```java
public class Main {
    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.configure()
                .setModel(model) // The model to train
                .setInputData(idata) // The input data
                .setEpochLimit(30000) // The number of epochs (iterations) allowed to train
                .setOutputData(odata) // The output data
                .setErrorThreshold(0.01) // Stop training when reaching this error threshold
                .setLearningRate(0.002) // The learning rate
                .setPrimaryTrainingStopper(Enums.trainingStoppers.EPOCH) // Affects the status unit on the loading bar
                .setStatusPrintingMethod("bar") // Display status as a loading bar rather than occasional updates
                .setTimeLimit(60); // Time limit in seconds
    }
}
```
This will take around 20 seconds depending on your computer's specs. Once a model is trained, you can save it. Models are saved as Lyra files, a custom-built file format that is still under development. To save it, simply use the `save()` method as shown below.

```java
public class Main {
    public static void main(String[] args) {
        LyraModel model = new LyraModel();
        
        // Model initialization and training...
        
        model.save("path/to/where/you/want/to/save/it"); 
    }
}
```

To load a model, create a new LyraModel and use the `load()` method.

```java
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

public class Main {
    public static void main(String[] args) {
        LyraModel loadedModel = new LyraModel();
        loadedModel.load("path/to/saved/model");
    }
}
```

## Documentation
- Javadoc (local): see the `docs/` directory in this repository. Open `docs/index-files/index-1.html` or `docs/io/github/equinoxelectronic/lyra2/package-summary.html` in a browser.
- API entry points: `io.github.equinoxelectronic.lyra2.objects.LyraModel`, `io.github.equinoxelectronic.lyra2.api.LyraModelBuilder`, `io.github.equinoxelectronic.lyra2.api.utility`.

## Examples
There are additional examples and tests under `src/test/java/`. A minimal runnable test harness exists under `src/test/java/io/github/equinoxelectronic/lyra2`.

To run the test suite:

```bash
mvn -q test
```

## Contributing
Contributions are welcome! If you find a bug or have a feature request:
- Open an issue with details and, if possible, steps to reproduce.
- For pull requests, keep changes focused and include tests when applicable.

Please also see any notes in `src/main/java/io/github/equinoxelectronic/lyra2/FOUND-ISSUES.md` for known areas needing attention.

## License
This project is licensed under the Apache License 2.0. See [LICENSE.md](LICENSE.md) for details.