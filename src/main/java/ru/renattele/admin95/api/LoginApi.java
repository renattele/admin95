package ru.renattele.admin95.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.dto.login.LoginResponse;

import jakarta.validation.Valid;

/**
 * API interface for handling login-related operations
 */
@RequestMapping("/login")
public interface LoginApi {
    
    /**
     * Displays the login page
     * 
     * @return the name of the login view
     */
    @GetMapping("/login")
    String login();
}