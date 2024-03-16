# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file to the working directory
COPY target/aichatbot-0.0.1-SNAPSHOT.jar .

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD sh -c 'sleep 90 && java -jar aichatbot-0.0.1-SNAPSHOT.jar'
