package ru.renattele.admin95.service.docker;

import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.model.docker.DockerProjectEntity;

public interface DockerComposeExecutorService {
    ProcessBuilder execute(DockerProjectDto project, String... args); // Add support for DTOs
    String getLogs(DockerProjectDto project); // Add support for DTOs
    String up(DockerProjectDto project); // Add support for DTOs
    String down(DockerProjectDto project); // Add support for DTOs
}
