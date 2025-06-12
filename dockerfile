# Use a minimal Java image
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

# Create and set working directory
WORKDIR /app

# Copy the JAR file
COPY target/cake-manager-*.jar app.jar

# Expose port
EXPOSE 9901

# Run the application
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
