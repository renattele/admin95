package ru.renattele.admin95.service.impl.docker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.service.docker.DockerComposeExecutorService;
import ru.renattele.admin95.service.docker.DockerLifecycleService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;

@Service
@RequiredArgsConstructor
public class DockerLifecycleServiceImpl implements DockerLifecycleService {
    private final DockerComposeExecutorService executor;
    private final DockerProjectManagementService dockerProjectManagementService;

    @Override
    public void stopProject(DockerProjectDto project) {
        executor.down(project);
        dockerProjectManagementService.updateProject(project);
    }

    @Override
    public void startProject(DockerProjectDto project) {
        executor.up(project);
        dockerProjectManagementService.updateProject(project);
    }
}
