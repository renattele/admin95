package ru.renattele.admin95.service.impl;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.mapper.UserMapper;
import ru.renattele.admin95.repository.UserRepository;
import ru.renattele.admin95.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Nullable
    @Override
    public UserDto getUserByName(String name) {
        return userRepository.findByNameEquals(name)
                .map(userMapper::toDto)
                .orElse(null);
    }

    @Override
    public UserDto createUser(String name,
                              String password,
                              List<UserDto.Role> roles,
                              boolean enabled) {
        var dto = UserDto.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .state(enabled ? UserDto.State.OK : UserDto.State.DISABLED)
                .createdAt(LocalDateTime.now())
                .build();
        var entity = userMapper.toEntity(dto);
        var savedEntity = userRepository.save(entity);
        return userMapper.toDto(savedEntity);
    }

    @Override
    public UserDto updateUser(String name,
                              String password,
                              List<UserDto.Role> roles,
                              boolean enabled) {
        var userDto = getUserByName(name);
        if (userDto == null) throw new ResourceNotFoundException();
        var updatedDtoBuilder = userDto.toBuilder()
                .name(name)
                .roles(roles)
                .state(enabled ? UserDto.State.OK : UserDto.State.DISABLED);
        if (password != null && !password.isEmpty()) updatedDtoBuilder.password(passwordEncoder.encode(password));
        var updatedDto = updatedDtoBuilder.build();
        var updatedEntity = userRepository.save(userMapper.toEntity(updatedDto));
        return userMapper.toDto(updatedEntity);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}