package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.api.TerminalApi;

@Controller
@RequiredArgsConstructor
public class TerminalController implements TerminalApi {
    @Value("${terminal.url}")
    private String terminalUrl;

    @Override
    public String dashboard(Model model) {
        model.addAttribute("terminalUrl", terminalUrl);
        return "terminal";
    }
}
