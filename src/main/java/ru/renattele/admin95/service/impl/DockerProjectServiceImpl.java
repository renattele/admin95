package ru.renattele.admin95.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.model.docker.DockerProjectEntity;
import ru.renattele.admin95.model.docker.ContainerEntity;
import ru.renattele.admin95.model.docker.ContainerState;
import ru.renattele.admin95.repository.DockerRepository;
import ru.renattele.admin95.service.DockerProjectService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class DockerProjectServiceImpl implements DockerProjectService {
    private final File projectsDir;
    private final DockerRepository dockerRepository;

    public DockerProjectServiceImpl(
            @Value("${DOCKER_PROJECTS_PATH}") String projectsPath,
            DockerRepository dockerRepository
    ) {
        projectsDir = new File(projectsPath);
        this.dockerRepository = dockerRepository;
        if (!projectsDir.exists()) {
            if (!projectsDir.mkdirs()) {
                throw new IllegalStateException("Cannot create projects directory: " + projectsPath);
            }
        }
    }

    @Override
    public List<DockerProjectEntity> getProjects() {
        var projectNames = getProjectNames();
        return projectNames.stream()
                .map(this::getProjectFor)
                .toList();
    }

    @Override
    public boolean stopProject(DockerProjectEntity project) {
        try {
            callDockerCompose(project.getConfigFile(), "down").start().waitFor();
            return true;
        } catch (Exception e) {
            log.error("Cannot stop project: {}", project.getName(), e);
            return false;
        }
    }

    @Override
    public boolean startProject(DockerProjectEntity project) {
        try {
            callDockerCompose(project.getConfigFile(), "up", "-d").start().waitFor();
            return true;
        } catch (Exception e) {
            log.error("Cannot start project: {}", project.getName(), e);
            return false;
        }
    }

    @Override
    public String logsForProject(DockerProjectEntity project) {
        var processBuilder = callDockerCompose(project.getConfigFile(), "logs", "--no-color");
        try (var reader = processBuilder.start().inputReader()) {
            return reader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            log.error("Cannot get logs for project: {}", project.getName(), e);
            return "";
        }
    }

    @Override
    public boolean createProject(String name) {
        var file = new File(projectsDir, name + ".yml");
        if (file.exists()) {
            return false;
        }
        try {
            if (!file.createNewFile()) {
                throw new IllegalStateException("Cannot create project file: " + file);
            }
            return true;
        } catch (Exception e) {
            log.error("Cannot create project file: {}", file, e);
            return false;
        }
    }

    @Override
    public boolean removeProject(DockerProjectEntity project) {
        if (!project.getConfigFile().delete()) {
            log.error("Cannot delete project file: {}", project.getConfigFile());
            return false;
        }
        return true;
    }

    private List<File> getProjectNames() {
        var array = projectsDir.listFiles();
        if (array == null) {
            throw new IllegalStateException("Cannot list files in projects directory because projectsDir.list() is null: " + projectsDir);
        }
        return Arrays.stream(array)
                .filter(file -> file.getName().endsWith(".yml") || file.getName().endsWith(".yaml"))
                .toList();
    }

    private DockerProjectEntity getProjectFor(File fileName) {
        var services = getProjectServices(fileName);
        var allContainers = dockerRepository.containers();
        var containers = allContainers.stream()
                .filter(container -> getConfigFileForContainer(container).endsWith(fileName.getName()) &&
                        services.contains(getServiceNameForContainer(container)))
                .toList();
        var allRunning = !containers.isEmpty() &&
                containers.stream().allMatch(container -> container.getState().equals(ContainerState.RUNNING));
        var state = allRunning ? ContainerState.RUNNING : ContainerState.STOPPED;
        return DockerProjectEntity.builder()
                .name(fileName.getName())
                .configFile(fileName)
                .containers(containers)
                .state(state)
                .build();
    }

    private String getConfigFileForContainer(ContainerEntity container) {
        return container.getLabels().getOrDefault("com.docker.compose.project.config_files", "");
    }

    private String getServiceNameForContainer(ContainerEntity container) {
        return container.getLabels().getOrDefault("com.docker.compose.service", "");
    }

    private List<String> getProjectServices(File fileName) {
        var processBuilder = callDockerCompose(fileName, "config", "--services");
        try (var reader = processBuilder.start().inputReader()) {
            return reader.lines().toList();
        } catch (Exception e) {
            log.error("Cannot get project services for fileName: {}", fileName, e);
            return List.of();
        }
    }

    private ProcessBuilder callDockerCompose(File file, String... args) {
        var processArgs = Stream.concat(
                Stream.of("docker", "compose", "-f", file.getAbsolutePath()),
                Arrays.stream(args)).toList();
        return new ProcessBuilder(processArgs);
    }
}
