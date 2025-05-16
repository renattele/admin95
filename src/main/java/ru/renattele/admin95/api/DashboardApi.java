package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Interface for dashboard controller functionality
 * Handles requests for system metrics visualization
 */
@Tag(name = "Dashboard", description = "System metrics dashboard operations")
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ROLE_ACCESS_DASHBOARD')")
public interface DashboardApi {

    /**
     * Display the dashboard page with system metrics
     * 
     * @param model The Spring MVC model for passing data to the view
     * @return The view name to render
     */
    @Operation(summary = "Display dashboard", description = "Shows the dashboard with system metrics and statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard successfully retrieved")
    })
    @GetMapping
    String dashboard(Model model);
}
