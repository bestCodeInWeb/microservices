# Stage 1: Build the application
FROM gradle:8.10-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Stage 2: Create the final image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -Djdk.disableLastUsageTracking=true -XX:MaxRAMPercentage=75.0"
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
