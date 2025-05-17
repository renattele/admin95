package ru.renattele.admin95.service.impl.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.service.docker.DockerComposeExecutorService;
import ru.renattele.admin95.util.ProcessUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Slf4j
@Service
public class DockerComposeExecutorServiceImpl implements DockerComposeExecutorService {
    private final File projectsDir;
    private final File envFile;

    public DockerComposeExecutorServiceImpl(
            @Value("${docker.path}") String projectsPath,
            @Value("${docker.env-path}") String envPath
    ) {
        projectsDir = new File(projectsPath);
        envFile = new File(envPath);
    }


    @Override
    public CompletableFuture<String> getLogs(DockerProjectDto project) {
        return executeAndGetLogs(project, "logs", "--no-color");
    }

    @Override
    public CompletableFuture<String> up(DockerProjectDto project) {
        return executeAndGetLogs(project, "up", "-d");
    }

    @Override
    public CompletableFuture<String> down(DockerProjectDto project) {
        return executeAndGetLogs(project, "down");
    }

    @Override
    public ProcessBuilder execute(DockerProjectDto project, String... args) {
        var projectDir = new File(projectsDir, project.getName());
        List<String> envFileArgs = envFile.exists() ? List.of("--env-file", envFile.getAbsolutePath()) : List.of();
        return ProcessUtil.builder(
                        Stream.concat(Stream.of("docker",
                                "compose",
                                "--project-directory",
                                projectDir.getAbsolutePath()
                        ), envFileArgs.stream()).toList(),
                        args)
                .redirectErrorStream(true);
    }

    private CompletableFuture<String> executeAndGetLogs(DockerProjectDto project, String... args) {
        var processBuilder = execute(project, args);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ProcessUtil.readInputStream(processBuilder.start());
            } catch (Exception e) {
                log.error("Cannot get logs for project: {}", project.getName(), e);
                return "";
            }
        });
    }
}
