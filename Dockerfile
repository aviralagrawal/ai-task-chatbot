FROM ubuntu:latest

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y software-properties-common && add-apt-repository -y ppa:openjdk-r/ppa && apt-get update && apt-get install -y openjdk-21-jdk && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN java -version
ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64

WORKDIR /app