FROM gradle:8-jdk21-alpine AS build
WORKDIR /app
COPY . .

RUN gradle :app-kafka:jar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/app-kafka/build/libs/*-all.jar ./app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
