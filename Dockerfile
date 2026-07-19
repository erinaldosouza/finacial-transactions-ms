# ---------- Build Stage ----------
FROM docker.io/maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

# Download dependencies first (better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM docker.io/eclipse-temurin:25-jre

WORKDIR /app

# Create a non-root user
RUN useradd --create-home spring

USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]