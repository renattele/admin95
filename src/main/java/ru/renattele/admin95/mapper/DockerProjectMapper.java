package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.model.docker.DockerProjectEntity;

public interface DockerProjectMapper {
    DockerProjectDto toDto(DockerProjectEntity entity);
    DockerProjectEntity toEntity(DockerProjectDto dto, DockerProjectDetailsDto dtoDetails);
}
