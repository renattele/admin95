package ru.renattele.admin95.dto.login;

import lombok.Data;

public sealed class LoginResponse {
    @Data
    public static final class Success extends LoginResponse {
        private String token;
    }

    @Data
    public static final class WrongCredentials extends LoginResponse {
        private String message;
    }
}
