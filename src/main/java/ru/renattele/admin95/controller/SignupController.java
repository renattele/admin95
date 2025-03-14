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
import ru.renattele.admin95.dto.signup.SignupRequest;
import ru.renattele.admin95.service.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userServiceImpl;

    @GetMapping
    public String signup(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping
    public String post(@Valid @ModelAttribute SignupRequest request,
                       BindingResult result,
                       Model model
                       ) {
        System.out.println(request);
        if (result.hasErrors()) {
            System.out.println("HAS ERRORS: " + result.getAllErrors());
            model.addAttribute("signupRequest", request);
            return "signup";
        }
        System.out.println(userServiceImpl.register(request));
        return "redirect:/";
    }
}
