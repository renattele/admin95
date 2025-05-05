package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API interface for home page operations
 */
@Tag(name = "Home", description = "Home page operations")
@RequestMapping("/")
public interface HomeApi {
    
    /**
     * Renders the home page
     * 
     * @return the view name for home page
     */
    @Operation(summary = "Get home page", description = "Retrieves the home page of the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Home page successfully retrieved")
    })
    @GetMapping("/")
    String home();
}
