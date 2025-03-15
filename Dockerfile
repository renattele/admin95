FROM openjdk:24-ea-17-jdk-slim-bullseye

## Wetty
RUN apt update && apt install npm python3 -y
RUN npm -g i wetty@v2.7.0

WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle
COPY src /app/src
RUN ./gradlew build --no-daemon
EXPOSE 8080


ENTRYPOINT ["./gradlew", "bootRun"]