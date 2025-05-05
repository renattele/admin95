package ru.renattele.admin95.service.docker;

import jakarta.annotation.Nullable;
import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;

import java.util.List;

public interface DockerProjectQueryService {
    List<DockerProjectDto> getProjects();

    @Nullable
    DockerProjectDto getProjectByName(String name);

    DockerProjectDetailsDto getProjectDetails(DockerProjectDto project);

    String logsForProject(DockerProjectDto project);
}
