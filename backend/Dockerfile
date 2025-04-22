# Use Java image
FROM openjdk:21-jdk

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download Maven dependencies (without source code)
RUN ./mvnw dependency:go-offline

# Now copy source code
COPY src src

# Build the project
RUN ./mvnw package -DskipTests

# Run the application
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]