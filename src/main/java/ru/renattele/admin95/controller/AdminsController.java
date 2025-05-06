package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.AdminApi;
import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.form.AdminForm;
import ru.renattele.admin95.service.UserService;

@Controller
@RequiredArgsConstructor
public class AdminsController implements AdminApi {
    private final UserService userService;

    @Override
    public String admins(Model model) {
        var users = userService.getUsers();
        model.addAttribute("admins", users);
        model.addAttribute("roles", UserDto.Role.values());
        return "admins";
    }

    @Override
    public void createAdmin(AdminForm form) {

    }

    @Override
    public void updateAdmin(AdminForm form) {

    }
}
