# Python-in-Java
This java class use a neural network that is written in python.
Neural network github [page](https://github.com/ayoolaolafenwa/PixelLib).

***Input:***   image.jpeg

***Output:*** JSON - the result of neural network
## Usage with docker
You can install docker [this](https://docs.docker.com/get-docker/)
```bash
docker build -t <name> .
docker run <name> .
```
## Usage without docker
### Installation

Use the package manager [pip](https://pip.pypa.io/en/stable/) to install python libs.

```bash
pip3 install setuptools
pip3 install protobuf==3.19.6
pip3 install tensorflow
pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu
pip3 install aiogram
```

### Start

```bash
mvn compile
mvn package 
java -jar target/PyInJava-1.0-SNAPSHOT-jar-with-dependencies.jar
```