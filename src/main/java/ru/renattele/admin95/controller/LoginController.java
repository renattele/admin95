package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.renattele.admin95.api.LoginApi;

@RequiredArgsConstructor
@Controller
public class LoginController implements LoginApi {
    @Override
    public String login() {
        return "login";
    }
}
