package ru.renattele.admin95.service.impl.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.service.docker.DockerComposeExecutorService;
import ru.renattele.admin95.util.ProcessUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class DockerComposeExecutorServiceImpl implements DockerComposeExecutorService {
    private final File projectsDir;
    private final File envFile;

    public DockerComposeExecutorServiceImpl(
            @Value("${docker.path}") String projectsPath,
            @Value("${docker.env-path}") String envPath
    ) throws IOException {
        projectsDir = new File(projectsPath);
        envFile = new File(envPath);
        if (!envFile.exists() && !envFile.createNewFile()) {
            throw new IllegalStateException("Cannot create .env file");
        }
    }


    @Override
    public String getLogs(DockerProjectDto project) {
        return executeAndGetLogs(project, "logs", "--no-color");
    }

    @Override
    public String up(DockerProjectDto project) {
        return executeAndGetLogs(project, "up", "-d");
    }

    @Override
    public String down(DockerProjectDto project) {
        return executeAndGetLogs(project, "down");
    }

    @Override
    public ProcessBuilder execute(DockerProjectDto project, String... args) {
        var projectDir = new File(projectsDir, project.getName());
        return ProcessUtil.builder(
                        List.of("docker",
                                "compose",
                                "--project-directory",
                                projectDir.getAbsolutePath(),
                                "--env-file",
                                envFile.getAbsolutePath()),
                        args)
                .redirectErrorStream(true);
    }

    private String executeAndGetLogs(DockerProjectDto project, String... args) {
        var processBuilder = execute(project, args);
        try {
            return ProcessUtil.readInputStream(processBuilder.start());
        } catch (Exception e) {
            log.error("Cannot get logs for project: {}", project.getName(), e);
            return "";
        }
    }
}
