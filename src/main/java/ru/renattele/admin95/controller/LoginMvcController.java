package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.renattele.admin95.api.LoginMvcApi;

@RequiredArgsConstructor
@Controller
public class LoginMvcController implements LoginMvcApi {
    @Override
    public String login() {
        return "login";
    }
}
