FROM openjdk:24-ea-17-jdk-slim-bullseye

RUN apt update && apt install nodejs

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY gradlew /app/
COPY gradle /app/gradle

COPY src /app/src

RUN ./gradlew build --no-daemon

EXPOSE 8080

ENTRYPOINT ["./gradlew", "bootRun"]