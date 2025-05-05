package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.api.LoginApi;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.service.UserService;

@RequiredArgsConstructor
@Controller
public class LoginController implements LoginApi {
    @Override
    public String login() {
        return "login";
    }
}
