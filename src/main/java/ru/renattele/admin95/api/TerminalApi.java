package ru.renattele.admin95.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API interface for terminal-related operations
 */
@RequestMapping("/admin/terminal")
public interface TerminalApi {
    
    /**
     * Displays the terminal dashboard page
     *
     * @param model the Spring MVC model to be populated with data
     * @return the name of the view to render
     */
    @GetMapping("/terminal")
    String dashboard(Model model);
}