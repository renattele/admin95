package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.mapper.TagMapper;
import ru.renattele.admin95.model.TagEntity;

@Service
public class TagMapperImpl implements TagMapper {

    @Override
    public TagDto toDto(TagEntity entity) {
        return TagDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public TagEntity toEntity(TagDto dto) {
        return TagEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
