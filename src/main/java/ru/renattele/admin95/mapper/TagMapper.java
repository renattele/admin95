package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.model.TagEntity;

public interface TagMapper {
    TagDto toDto(TagEntity entity);

    TagEntity toEntity(TagDto dto);
}
