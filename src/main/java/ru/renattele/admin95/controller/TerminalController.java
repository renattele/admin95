package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.TerminalApi;

@Controller
@RequiredArgsConstructor
public class TerminalController implements TerminalApi {
    @Override
    public String dashboard(Model model) {
        return "terminal";
    }
}
