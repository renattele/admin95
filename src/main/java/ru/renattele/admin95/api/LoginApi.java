package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication operations")
@RequestMapping("/login")
public interface LoginApi {
    
    /**
     * Displays the login page
     * 
     * @return the name of the login view
     */
    @Operation(summary = "Display login page", description = "Shows the login form for user authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login page successfully retrieved")
    })
    @GetMapping("/login")
    String login();
}
