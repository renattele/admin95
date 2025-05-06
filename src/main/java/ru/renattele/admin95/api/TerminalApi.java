package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API interface for terminal-related operations
 */
@Tag(name = "Terminal", description = "Terminal operations")
@RequestMapping("/admin/terminal")
public interface TerminalApi {
    
    /**
     * Displays the terminal dashboard page
     *
     * @param model the Spring MVC model to be populated with data
     * @return the name of the view to render
     */
    @Operation(summary = "Display terminal dashboard", description = "Shows the terminal dashboard interface")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal dashboard successfully retrieved")
    })
    @GetMapping
    String dashboard(Model model);
}
