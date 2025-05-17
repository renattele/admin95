package ru.renattele.admin95.service.docker;

import ru.renattele.admin95.dto.DockerProjectDto;

import java.util.concurrent.CompletableFuture;

public interface DockerComposeExecutorService {
    ProcessBuilder execute(DockerProjectDto project, String... args);
    CompletableFuture<String> getLogs(DockerProjectDto project);
    CompletableFuture<String> up(DockerProjectDto project);
    CompletableFuture<String> down(DockerProjectDto project);
}
