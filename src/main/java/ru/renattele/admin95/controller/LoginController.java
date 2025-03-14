package ru.renattele.admin95.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.dto.login.LoginRequest;
import ru.renattele.admin95.repository.DockerRepository;
import ru.renattele.admin95.service.DockerProjectService;
import ru.renattele.admin95.service.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @GetMapping
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping
    public String post(@Valid @ModelAttribute LoginRequest request,
                       BindingResult result,
                       Model model) {
        System.out.println(request);
        if (result.hasErrors()) {
            System.out.println("HAS ERRORS: " + result.getAllErrors());
            model.addAttribute("loginRequest", request);
            return "login";
        }
        System.out.println(userService.authenticate(request));
        return "redirect:/";
    }
}
