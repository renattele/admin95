package ru.renattele.admin95.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.form.AdminForm;

@RequestMapping("/admin/admins")
@PreAuthorize("principal.username == @environment.getProperty('users.default')")
public interface AdminMvcApi {
    @GetMapping
    String admins(Model model);

    @PostMapping
    @ResponseBody
    void createAdmin(@RequestBody AdminForm form);

    @PutMapping
    @ResponseBody
    void updateAdmin(@RequestBody AdminForm form);

    @DeleteMapping("/{id}")
    @ResponseBody
    void deleteAdmin(@PathVariable("id") Long id);
}
