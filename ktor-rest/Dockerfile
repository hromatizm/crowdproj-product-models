FROM gradle:8-jdk21-alpine AS build
WORKDIR /app
COPY .. .

RUN gradle :ktor:buildFatJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/ktor-rest/build/libs/*-all.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
