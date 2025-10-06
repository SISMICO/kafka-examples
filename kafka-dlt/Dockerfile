# Multi-stage build for kafka-dlt
FROM gradle:8.14.3-jdk17-noble AS builder

# Set working directory
WORKDIR /app

# Copy gradle files
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/

# Copy source code
COPY src/ src/

# Build the application
RUN gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:17-jre-noble

# Set working directory
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV KAFKA_BOOTSTRAP_SERVERS=broker:29092

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
