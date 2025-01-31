# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file (`mvn clean package` first)
COPY target/*.jar app.jar

# Expose port
EXPOSE 2660

# Run the service
CMD ["java", "-jar", "app.jar"]

# docker build -t cloud-storage-service:latest -f cloud-storage-service/Dockerfile cloud-storage-service