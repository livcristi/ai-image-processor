# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file
COPY target/*.jar app.jar

# Expose port
EXPOSE 2740

# Run the service
CMD ["java", "-jar", "app.jar"]
