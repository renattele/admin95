package ru.renattele.admin95.service;

import ru.renattele.admin95.dto.UserDto;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDto user);

    boolean isTokenValid(String token, UserDto user);
}
