package ru.renattele.admin95.service.docker;

import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;

public interface DockerProjectManagementService {
    DockerProjectDto createProject(String name);

    void updateProject(DockerProjectDto project);

    void updateProjectDetails(DockerProjectDetailsDto project);

    void removeProject(DockerProjectDto project);
}
