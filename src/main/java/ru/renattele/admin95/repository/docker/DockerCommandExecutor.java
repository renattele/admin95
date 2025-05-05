package ru.renattele.admin95.repository.docker;

import ru.renattele.admin95.model.docker.ContainerState;

public interface DockerCommandExecutor {
    void up(String name) throws DockerException;
    void down(String name) throws DockerException;
    String logs(String name) throws DockerException;
    ContainerState state(String name) throws DockerException;
}