FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test -x validateStructure --no-daemon

# Fase runtime !
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]
