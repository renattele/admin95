package ru.renattele.admin95.dto.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Name should not be empty")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
