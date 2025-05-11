package ru.renattele.admin95.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error")
public interface ErrorApi {
    @RequestMapping
    String handleError(HttpServletRequest request);
}
