package ru.renattele.admin95.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.dto.JwtAuthenticationResponse;
import ru.renattele.admin95.dto.login.LoginRequest;

@RequestMapping("/api/auth")
public interface AuthApi {
    @GetMapping("/check/terminal")
    @PreAuthorize("hasRole('ROLE_ACCESS_TERMINAL')")
    void check();

    @PostMapping("/login")
    JwtAuthenticationResponse login(@RequestBody LoginRequest loginRequest);
}
