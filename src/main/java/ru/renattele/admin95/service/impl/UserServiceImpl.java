package ru.renattele.admin95.service.impl;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.UserDto;
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
    public UserDto createUser(String name, String password, List<UserDto.Role> roles) {
        var dto = UserDto.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .state(UserDto.State.OK)
                .createdAt(LocalDateTime.now())
                .build();
        var entity = userMapper.toEntity(dto);
        var savedEntity = userRepository.save(entity);
        return userMapper.toDto(savedEntity);
    }

    @Override
    public boolean updatePassword(String name, String oldPassword, String newPassword) {
        var entity = userRepository.findByNameEquals(name);
        if (entity.isEmpty()) return false;
        if (passwordEncoder.matches(oldPassword, entity.get().getPassword())) return false;
        entity.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(entity.get());
        return true;
    }

    @Override
    public boolean updateRoles(String name, List<UserDto.Role> roles) {
        var entity = userRepository.findByNameEquals(name);
        if (entity.isEmpty()) return false;
        var dto = userMapper.toDto(entity.get());
        dto.setRoles(roles);

        var newEntity = userMapper.toEntity(dto);
        userRepository.save(newEntity);
        return true;
    }

    @Override
    public boolean updateState(String name, UserDto.State state) {
        var entity = userRepository.findByNameEquals(name);
        if (entity.isEmpty()) return false;
        var dto = userMapper.toDto(entity.get());
        dto.setState(state);

        var newEntity = userMapper.toEntity(dto);
        userRepository.save(newEntity);
        return true;
    }
}