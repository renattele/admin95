package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.model.docker.DockerProjectDetailsEntity;

public interface DockerProjectDetailsMapper {
    DockerProjectDetailsDto toDto(DockerProjectDetailsEntity entity);

    DockerProjectDetailsEntity toEntity(DockerProjectDetailsDto dto);
}
