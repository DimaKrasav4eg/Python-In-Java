FROM maven:3.8.3-ibmjava

WORKDIR /opt/app
COPY pom.xml ./
COPY ./src ./src
RUN mvn -f /opt/app/pom.xml clean install \
    && apt-get update  \
    && apt-get upgrade -y \
    && apt-get install --no-install-recommends -y ffmpeg \
    && apt-get install --no-install-recommends -y python3-pip \
    && rm -rf /var/lib/apt/lists/* \
    # Install python libs
    && pip3 install --upgrade pip \
    && pip3 install --upgrade setuptools \
    && pip3 install protobuf==3.19.6 \
    # [Tensorflow]
    && pip3 install --no-cache-dir tensorflow \
    && pip3 cache purge \
    # [pyTorch]
    && pip3 install --no-cache-dir torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu \
    && pip3 cache purge \
    # [Aiogram]
    && pip3  install --no-cache-dir aiogram \
    && pip3 cache purge

# Copy the built JAR file from the build stage
COPY target/PyInJava-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/app/app.jar

# Set the entry point for the container
CMD ["java", "-jar", "/opt/app/app.jar"]