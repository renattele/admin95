package ru.renattele.admin95.service.impl.docker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.mapper.DockerProjectDetailsMapper;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.repository.DockerProjectRepository;
import ru.renattele.admin95.service.docker.DockerComposeExecutorService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;
import ru.renattele.admin95.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerProjectManagementServiceImpl implements DockerProjectManagementService {
    private final DockerProjectRepository dockerProjectRepository;
    private final ObjectMapper objectMapper;
    private final DockerComposeExecutorService executor;
    private final DockerProjectMapper mapper;
    private final DockerProjectDetailsMapper dockerProjectDetailsMapper;

    @Value("${docker.path}")
    private String projectsPath;

    @Value("${docker.default-compose-file}")
    private String defaultComposeFile;

    @Override
    public DockerProjectDto createProject(String name) {
        var existingProject = dockerProjectRepository.findDockerProjectEntityByNameEquals(name);
        if (existingProject.isPresent()) return null;
        var projectDto = DockerProjectDto.builder()
                .name(name)
                .state(DockerProjectDto.ContainerState.STOPPED)
                .build();
        var detailsDto = DockerProjectDetailsDto.builder()
                .content("")
                .logs("")
                .build();
        updateProjectInfo(projectDto, detailsDto);
        dockerProjectRepository.save(mapper.toEntity(projectDto, detailsDto));
        return projectDto;
    }

    @Override
    public void updateProject(DockerProjectDto projectDto) {
        var entity = dockerProjectRepository.findById(projectDto.getId()).orElseThrow(ResourceNotFoundException::new);
        var details = dockerProjectDetailsMapper.toDto(entity.getDetails());
        updateProjectInfo(projectDto, details);
        dockerProjectRepository.save(mapper.toEntity(projectDto, details));
    }

    @Override
    public void updateProjectDetails(DockerProjectDetailsDto project) {
        var projectEntity = dockerProjectRepository.findDockerProjectEntityByNameEquals(project.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + project.getName()));
        var details = projectEntity.getDetails();
        details.setContent(project.getContent());
        details.setLogs(project.getLogs());
        dockerProjectRepository.save(projectEntity);
    }

    @Override
    public void removeProject(DockerProjectDto projectDto) {
        dockerProjectRepository.deleteById(projectDto.getId());
        deleteProjectFiles(projectDto.getName());
    }

    private void deleteProjectFiles(String projectName) {
        var dir = new File(projectsPath, projectName);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            log.error("Cannot delete project directory: {}", dir, e);
        }
    }

    private void updateProjectInfo(DockerProjectDto project, DockerProjectDetailsDto projectDetails) {
        project.setState(projectState(project));

        var dir = new File(projectsPath, project.getName());
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Cannot create project directory: " + dir);
        }
        var file = new File(dir, defaultComposeFile);
        try {
            FileUtils.write(file, projectDetails.getContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write to project file: " + file, e);
        }
    }

    private DockerProjectDto.ContainerState projectState(DockerProjectDto project) {
        var process = executor.execute(project, "ls", "--format", "json");
        try {
            var json = IOUtil.readFromReader(process.start().inputReader());
            var response = objectMapper.readValue(json,
                    new TypeReference<List<DockerComposeLsResponse>>() {
                    });
            var s = response.stream().filter(element ->
                    Objects.equals(element.getName(), project.getName())).findFirst();
            if (s.isEmpty()) return DockerProjectDto.ContainerState.STOPPED;
            return containerStateFromString(s.get().getState());
        } catch (IOException e) {
            log.error("Cannot get project state: {}", project.getName(), e);
            return DockerProjectDto.ContainerState.STOPPED;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DockerComposeLsResponse {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Status")
        private String state;
    }

    private DockerProjectDto.ContainerState containerStateFromString(String state) {
        if (state.contains("running")) return DockerProjectDto.ContainerState.RUNNING;
        if (state.contains("paused")) return DockerProjectDto.ContainerState.PAUSED;
        if (state.contains("exited")) return DockerProjectDto.ContainerState.EXITED;
        return DockerProjectDto.ContainerState.STOPPED;
    }
}
