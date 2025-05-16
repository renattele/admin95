FROM openjdk:21-ea-21-jdk-slim-bullseye AS build

WORKDIR /app

COPY gradlew build.gradle.kts settings.gradle.kts /app/
COPY gradle /app/gradle

RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

COPY src /app/src

RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon

FROM openjdk:21-ea-21-slim-bullseye

# Install Docker Compose, Docker CLI and RAR
RUN sed -i -e's/ main/ main contrib non-free/g' /etc/apt/sources.list && \
    apt update && \
    apt install ca-certificates curl rar -y && \
    install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc && \
    chmod a+r /etc/apt/keyrings/docker.asc && \
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt update && \
    apt install docker-ce-cli docker-compose-plugin -y

RUN apt install rar

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
