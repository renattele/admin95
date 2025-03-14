package ru.renattele.admin95.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.dto.login.LoginResponse;
import ru.renattele.admin95.dto.signup.SignupRequest;
import ru.renattele.admin95.dto.signup.SignupResponse;
import ru.renattele.admin95.model.UserEntity;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserEntity> getAllUsers();

    void saveUser(UserEntity user);

    SignupResponse register(SignupRequest request);

    LoginResponse authenticate(LoginRequest request);
}
