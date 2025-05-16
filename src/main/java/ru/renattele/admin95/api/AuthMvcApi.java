package ru.renattele.admin95.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public interface AuthMvcApi {
    @GetMapping("/check/terminal")
    @CrossOrigin
    @PreAuthorize("hasRole('ACCESS_TERMINAL')")
    ResponseEntity<String> check();
}