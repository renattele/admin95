package ru.renattele.admin95.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.AdminMvcApi;
import ru.renattele.admin95.dto.UserDto;
import ru.renattele.admin95.form.AdminForm;
import ru.renattele.admin95.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AdminsController implements AdminMvcApi {
    private final UserService userService;

    @Override
    public String admins(Model model) {
        var users = userService.getUsers();
        model.addAttribute("admins", users);
        model.addAttribute("roles", UserDto.Role.values());
        return "admins";
    }

    @Override
    public void createAdmin(@Valid AdminForm form) {
        userService.createUser(
                form.getName(),
                form.getPassword(),
                rolesFromForm(form),
                form.isEnabled()
        );
    }

    @Override
    public void updateAdmin(@Valid AdminForm form) {
        userService.updateUser(
                form.getName(),
                form.getPassword(),
                rolesFromForm(form),
                form.isEnabled()
        );
    }

    @Override
    public void deleteAdmin(Long id) {
        userService.deleteUser(id);
    }

    private List<UserDto.Role> rolesFromForm(AdminForm form) {
        return Arrays.stream(new UserDto.Role[]{
                form.isAccessDashboard() ? UserDto.Role.ACCESS_DASHBOARD : null,
                form.isAccessContainers() ? UserDto.Role.ACCESS_CONTAINERS : null,
                form.isAccessTerminal() ? UserDto.Role.ACCESS_TERMINAL : null,
                form.isAccessBackups() ? UserDto.Role.ACCESS_BACKUPS : null,
        }).filter(Objects::nonNull).toList();
    }
}
