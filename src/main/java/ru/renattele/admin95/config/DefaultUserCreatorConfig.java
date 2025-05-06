package ru.renattele.admin95.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.service.PasswordGeneratorService;
import ru.renattele.admin95.service.UserService;

import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DefaultUserCreatorConfig {

    private final PasswordGeneratorService passwordGeneratorService;
    private final UserService userService;
    @Value("${users.default}")
    private String defaultUser;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (userService.getUserByName(defaultUser) == null) {
            createUser();
        }
    }

    private void createUser() {
        var username = defaultUser;
        var password = passwordGeneratorService.generatePassword();
        userService.createUser(username, password,
                Arrays.stream(UserDto.Role.values()).toList()
        );
        var message = generateMessage(username, password).lines();
        message.forEach(log::warn);
    }

    private String generateMessage(String username, String password) {
        return """
                ******************************************************
                
                
                
                No Admin user found in database!
                Default user created:
                Username: %s
                Password: %s
                Please save it! It won't show again!
                
                
                
                ******************************************************
                """.formatted(username, password);
    }
}
