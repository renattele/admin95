package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.model.UserEntity;

public interface UserMapper {
    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity entity);
}
