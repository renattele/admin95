package ru.renattele.admin95.dto.signup;


import lombok.Data;

public sealed class SignupResponse {
    @Data
    public static final class Success extends SignupResponse {
        private String token;
    }

    @Data
    public static final class UserAlreadyExists extends SignupResponse {
        private String message;
    }
}
