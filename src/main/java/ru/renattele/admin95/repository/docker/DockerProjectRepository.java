package ru.renattele.admin95.repository.docker;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.renattele.admin95.model.docker.DockerProjectEntity;

import java.util.Optional;

public interface DockerProjectRepository extends JpaRepository<DockerProjectEntity, Long> {
    Optional<DockerProjectEntity> findDockerProjectEntityByNameEquals(String name);
}
