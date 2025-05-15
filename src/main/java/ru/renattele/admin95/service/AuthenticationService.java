package ru.renattele.admin95.service;

import ru.renattele.admin95.dto.JwtAuthenticationResponse;
import ru.renattele.admin95.dto.login.LoginRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse authenticate(LoginRequest loginRequest);
}
