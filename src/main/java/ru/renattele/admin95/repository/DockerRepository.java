package ru.renattele.admin95.repository;

import ru.renattele.admin95.model.docker.ContainerEntity;

import java.util.List;

public interface DockerRepository {
   String logsFor(ContainerEntity container);
   List<ContainerEntity> containers();
}
