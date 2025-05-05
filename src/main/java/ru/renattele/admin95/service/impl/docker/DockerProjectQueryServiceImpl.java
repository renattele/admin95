package ru.renattele.admin95.service.impl.docker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.mapper.DockerProjectDetailsMapper;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.repository.docker.DockerProjectRepository;
import ru.renattele.admin95.service.docker.DockerComposeExecutorService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DockerProjectQueryServiceImpl implements DockerProjectQueryService {
    private final DockerProjectRepository dockerProjectRepository;
    private final DockerProjectManagementService dockerProjectManagementService;
    private final DockerComposeExecutorService executor;
    private final DockerProjectMapper dockerProjectMapper;
    private final DockerProjectDetailsMapper dockerProjectDetailsMapper;

    @Override
    public List<DockerProjectDto> getProjects() {
        var all = dockerProjectRepository.findAll();

        all.forEach(project ->
                dockerProjectManagementService.updateProject(dockerProjectMapper.toDto(project)));

        return all.stream()
                .map(dockerProjectMapper::toDto)
                .sorted(Comparator.comparing(DockerProjectDto::getName))
                .toList();
    }

    @Override
    public DockerProjectDto getProjectByName(String name) {
        return dockerProjectRepository.findDockerProjectEntityByNameEquals(name)
                .map(project -> {
                    dockerProjectManagementService.updateProject(dockerProjectMapper.toDto(project));
                    return dockerProjectMapper.toDto(project);
                })
                .orElse(null);
    }

    @Override
    public DockerProjectDetailsDto getProjectDetails(DockerProjectDto project) {
        var dockerProjectEntity = dockerProjectRepository.findById(project.getId());
        if (dockerProjectEntity.isEmpty()) {
            return null;
        }
        var details = dockerProjectEntity.get().getDetails();
        return dockerProjectDetailsMapper.toDto(details);
    }

    @Override
    public String logsForProject(DockerProjectDto project) {
        var details = getProjectDetails(project);
        var entity = dockerProjectMapper.toEntity(project, details);
        var logs = executor.getLogs(project);
        if (!logs.isEmpty() && details != null) {
            details.setLogs(logs);
        }
        dockerProjectManagementService.updateProjectDetails(details);
        return entity.getDetails().getLogs();
    }
}
