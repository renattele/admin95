package ru.renattele.admin95.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.dto.JwtAuthenticationResponse;
import ru.renattele.admin95.dto.LoginRequestDto;

@RequestMapping("/api/auth")
public interface AuthApi {
    @PostMapping("/login")
    JwtAuthenticationResponse login(@RequestBody @Valid LoginRequestDto loginRequest);
}
