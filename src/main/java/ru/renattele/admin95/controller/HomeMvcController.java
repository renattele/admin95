package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.renattele.admin95.api.HomeMvcApi;

@Controller
@RequiredArgsConstructor
public class HomeMvcController implements HomeMvcApi {
    @Override
    public String home() {
        return "index";
    }
}
