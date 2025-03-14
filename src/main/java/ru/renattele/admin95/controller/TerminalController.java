package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/terminal")
@RequiredArgsConstructor
public class TerminalController {
    @GetMapping
    public String dashboard() {
        return "terminal";
    }
}
