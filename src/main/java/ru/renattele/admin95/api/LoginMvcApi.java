package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API interface for handling login-related operations
 */
@Tag(name = "Authentication", description = "Authentication operations")
@RequestMapping("/login")
public interface LoginMvcApi {
    
    /**
     * Displays the login page
     * 
     * @return the name of the login view
     */
    @Operation(summary = "Display login page", description = "Shows the login form for user authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login page successfully retrieved")
    })
    @GetMapping
    String login();
}
