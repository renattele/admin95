package ru.renattele.admin95.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API interface for home page operations
 */
@RequestMapping("/")
public interface HomeApi {
    
    /**
     * Renders the home page
     * 
     * @return the view name for home page
     */
    @GetMapping("/")
    String home();
}