package ru.renattele.admin95.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.DockerProjectDetailsDto;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.mapper.DockerProjectDetailsMapper;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.mapper.TagMapper;
import ru.renattele.admin95.model.docker.ContainerState;
import ru.renattele.admin95.model.docker.DockerProjectEntity;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DockerProjectMapperImpl implements DockerProjectMapper {
    private final DockerProjectDetailsMapper dockerProjectDetailsMapper;
    private final TagMapper tagMapper;

    @Override
    public DockerProjectDto toDto(DockerProjectEntity entity) {
        return DockerProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .link(entity.getLink())
                .state(toDtoState(entity.getState()))
                .tags(entity.getTags()
                        .stream().map(tagMapper::toDto).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public DockerProjectEntity toEntity(DockerProjectDto dto, DockerProjectDetailsDto dtoDetails) {
        var details = dockerProjectDetailsMapper.toEntity(dtoDetails);
        var result = DockerProjectEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .link(dto.getLink())
                .state(toEntityState(dto.getState()))
                .details(details)
                .tags(dto.getTags() != null ? dto.getTags().stream().map(tagMapper::toEntity)
                        .collect(Collectors.toSet()) : new HashSet<>())
                .build();
        result.getDetails().setProject(result);
        return result;
    }

    private ContainerState toEntityState(DockerProjectDto.ContainerState state) {
        return switch (state) {
            case RUNNING -> ContainerState.RUNNING;
            case STOPPED -> ContainerState.STOPPED;
            case PAUSED -> ContainerState.PAUSED;
            case EXITED -> ContainerState.EXITED;
            case UNKNOWN -> ContainerState.UNKNOWN;
        };
    }

    private DockerProjectDto.ContainerState toDtoState(ContainerState state) {
        return switch (state) {
            case RUNNING -> DockerProjectDto.ContainerState.RUNNING;
            case STOPPED -> DockerProjectDto.ContainerState.STOPPED;
            case PAUSED -> DockerProjectDto.ContainerState.PAUSED;
            case EXITED -> DockerProjectDto.ContainerState.EXITED;
            case UNKNOWN -> DockerProjectDto.ContainerState.UNKNOWN;
        };
    }
}
