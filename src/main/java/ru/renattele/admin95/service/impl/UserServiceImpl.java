package ru.renattele.admin95.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.dto.login.LoginResponse;
import ru.renattele.admin95.dto.signup.SignupRequest;
import ru.renattele.admin95.dto.signup.SignupResponse;
import ru.renattele.admin95.model.UserEntity;
import ru.renattele.admin95.repository.UserRepository;
import ru.renattele.admin95.service.UserService;

import java.util.List;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public SignupResponse register(SignupRequest request) {
        var user = userRepository.findByNameEquals(request.getName());
        if (user.isPresent()) return new SignupResponse.UserAlreadyExists();
        var encodedPassword = passwordEncoder.encode(request.getPassword());
        var newUser = UserEntity.builder()
                .name(request.getName())
                .password(encodedPassword)
                .build();
        userRepository.save(newUser);
        return new SignupResponse.Success();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        var user = userRepository.findByNameEquals(request.getName());
        if (user.isEmpty()) return new LoginResponse.WrongCredentials();
        var passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.get().getPassword());

        if (passwordMatches) {
            return new LoginResponse.Success();
        } else {
            return new LoginResponse.WrongCredentials();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByNameEquals(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.builder()
                .username(user.get().getName())
                .passwordEncoder(passwordEncoder::encode)
                .password(user.get().getPassword())
                .roles("USER")
                .build();
    }
}