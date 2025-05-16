package ru.renattele.admin95.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.renattele.admin95.api.AuthApi;
import ru.renattele.admin95.dto.JwtAuthenticationResponse;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthenticationService authenticationService;

    @Override
    public JwtAuthenticationResponse login(@Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
