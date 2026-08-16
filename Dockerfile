# --- Build Stage ---
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Cache dependencies first to speed up subsequent builds
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/problem-submission-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]