package ru.renattele.admin95.repository.impl.docker;

import ru.renattele.admin95.model.docker.ContainerState;
import ru.renattele.admin95.repository.docker.DockerCommandExecutor;

public class DockerCommandExecutorImpl implements DockerCommandExecutor {
    @Override
    public void up(String name) {

    }

    @Override
    public void down(String name) {

    }

    @Override
    public String logs(String name) {
        return "";
    }

    @Override
    public ContainerState state(String name) {
        return null;
    }
}
