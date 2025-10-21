# ===== Stage 1: Build the application =====
FROM maven:3.9.11-eclipse-temurin-21 AS builder


WORKDIR /app

# Copy everything (no src at root)
COPY pom.xml .
COPY common/pom.xml ./common/pom.xml
COPY network/pom.xml ./network/pom.xml
COPY server/pom.xml ./server/pom.xml

# Download dependencies for all modules
RUN mvn dependency:go-offline -B

# Copy all sources now
COPY . .

# Build the entire multi-module project
RUN mvn clean package -DskipTests

# ===== Stage 2: Choose which module to run =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Example: copy the runnable JAR from one module
COPY --from=builder /app/server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]