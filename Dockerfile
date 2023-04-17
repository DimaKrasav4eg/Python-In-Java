FROM debian:11-slim

# Update package lists and install ffmpeg and other dependencies
RUN apt-get update && apt-get install -y ffmpeg python3 python3-pip nano wget

# Install python libs
# [pyTorch]
RUN pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu
# [Tensorflow]
RUN pip install tensorflow
# [Aiogram]
RUN pip install aiogram

# Copy JAR-file in container
COPY target/PyInJava-1.0-SNAPSHOT.jar /PyInJava-1.0-SNAPSHOT.jar

# Setting the working directory to the root of the container
WORKDIR /

# Start app
CMD ["java", "-jar", "/PyInJava-1.0-SNAPSHOT.jar"]
