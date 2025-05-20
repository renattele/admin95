package ru.renattele.admin95.service;

import ru.renattele.admin95.dto.JwtAuthenticationResponse;
import ru.renattele.admin95.dto.LoginRequestDto;

public interface AuthenticationService {
    JwtAuthenticationResponse authenticate(LoginRequestDto loginRequest);
}
