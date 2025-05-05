package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.api.HomeApi;

@Controller
@RequiredArgsConstructor
public class HomeController implements HomeApi {
    @Override
    public String home() {
        return "index";
    }
}
