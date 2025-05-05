package ru.renattele.admin95.service.docker;

import ru.renattele.admin95.dto.DockerProjectDto;

public interface DockerLifecycleService {
    void stopProject(DockerProjectDto project);

    void startProject(DockerProjectDto project);
}
