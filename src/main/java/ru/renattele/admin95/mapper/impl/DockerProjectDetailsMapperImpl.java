package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.mapper.DockerProjectDetailsMapper;
import ru.renattele.admin95.model.docker.DockerProjectDetailsEntity;

@Service
public class DockerProjectDetailsMapperImpl implements DockerProjectDetailsMapper {
    @Override
    public DockerProjectDetailsDto toDto(DockerProjectDetailsEntity entity) {
        return DockerProjectDetailsDto.builder()
                .id(entity.getId())
                .name(entity.getProject().getName())
                .logs(entity.getLogs())
                .content(entity.getContent())
                .build();
    }

    @Override
    public DockerProjectDetailsEntity toEntity(DockerProjectDetailsDto dto) {
        return DockerProjectDetailsEntity.builder()
                .id(dto.getId())
                .logs(dto.getLogs())
                .content(dto.getContent())
                .build();
    }
}
