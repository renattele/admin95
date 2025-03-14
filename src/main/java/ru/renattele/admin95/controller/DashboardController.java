package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    @GetMapping
    public String dashboard(Model model) {
        var url = "https://quickchart.io/chart/render/zm-d75e5a96-f26c-4412-a21a-35d408495fcc?f=.png";
        model.addAttribute("CPU_USAGE_URL", url);
        model.addAttribute("CPU_TEMPERATURE_URL", url);
        model.addAttribute("RAM_URL", url);
        model.addAttribute("DISK_URL", url);
        return "dashboard";
    }
}
