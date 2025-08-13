# Base image with a specific version of OpenJDK and Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Final image to run the application
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]