package ru.renattele.admin95.service;

import org.jetbrains.annotations.Nullable;
import ru.renattele.admin95.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    long countUsers();

    @Nullable UserDto getUserByName(String name);

    UserDto createUser(
            String name,
            String password,
            List<UserDto.Role> roles,
            boolean enabled
    );

    UserDto updateUser(
            String name,
            @Nullable String password,
            List<UserDto.Role> roles,
            boolean enabled
    );

    void deleteUser(Long id);
}
