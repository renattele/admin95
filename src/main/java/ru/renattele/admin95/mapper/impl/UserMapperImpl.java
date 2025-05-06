package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.mapper.UserMapper;
import ru.renattele.admin95.model.UserEntity;

@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .password(dto.getPassword())
                .roles(dto.getRoles().stream().map(this::toEntityRole).toList())
                .state(toEntityState(dto.getState()))
                .createdAt(dto.getCreatedAt())
                .lastAccessedAt(dto.getLastAccessedAt())
                .build();
    }

    @Override
    public UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .password(entity.getPassword())
                .roles(entity.getRoles().stream().map(this::toDtoRole).toList())
                .state(toDtoState(entity.getState()))
                .createdAt(entity.getCreatedAt())
                .lastAccessedAt(entity.getLastAccessedAt())
                .build();
    }

    private UserDto.Role toDtoRole(UserEntity.Role role) {
        switch (role) {
            case ACCESS_DASHBOARD -> {
                return UserDto.Role.ACCESS_DASHBOARD;
            }
            case ACCESS_CONTAINERS -> {
                return UserDto.Role.ACCESS_CONTAINERS;
            }
            case ACCESS_TERMINAL -> {
                return UserDto.Role.ACCESS_TERMINAL;
            }
            case ACCESS_BACKUPS -> {
                return UserDto.Role.ACCESS_BACKUPS;
            }
            case ACCESS_USERS -> {
                return UserDto.Role.ACCESS_USERS;
            }
            default -> throw new IllegalStateException("Unexpected value: " + role);
        }
    }

    private UserEntity.Role toEntityRole(UserDto.Role role) {
        switch (role) {
            case ACCESS_DASHBOARD -> {
                return UserEntity.Role.ACCESS_DASHBOARD;
            }
            case ACCESS_CONTAINERS -> {
                return UserEntity.Role.ACCESS_CONTAINERS;
            }
            case ACCESS_TERMINAL -> {
                return UserEntity.Role.ACCESS_TERMINAL;
            }
            case ACCESS_BACKUPS -> {
                return UserEntity.Role.ACCESS_BACKUPS;
            }
            case ACCESS_USERS -> {
                return UserEntity.Role.ACCESS_USERS;
            }
            default -> throw new IllegalStateException("Unexpected value: " + role);
        }
    }

    private UserDto.State toDtoState(UserEntity.State state) {
        switch (state) {
            case OK -> {
                return UserDto.State.OK;
            }
            case DISABLED -> {
                return UserDto.State.DISABLED;
            }
            default -> throw new IllegalStateException("Unexpected value: " + state);
        }
    }

    private UserEntity.State toEntityState(UserDto.State state) {
        switch (state) {
            case OK -> {
                return UserEntity.State.OK;
            }
            case DISABLED -> {
                return UserEntity.State.DISABLED;
            }
            default -> throw new IllegalStateException("Unexpected value: " + state);
        }
    }
}
