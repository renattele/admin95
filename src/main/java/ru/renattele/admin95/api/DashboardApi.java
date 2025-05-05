package ru.renattele.admin95.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Interface for dashboard controller functionality
 * Handles requests for system metrics visualization
 */
@RequestMapping("/admin/dashboard")
public interface DashboardApi {

    /**
     * Display the dashboard page with system metrics
     * 
     * @param model The Spring MVC model for passing data to the view
     * @return The view name to render
     */
    @GetMapping
    String dashboard(Model model);
}