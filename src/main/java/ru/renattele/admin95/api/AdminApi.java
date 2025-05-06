package ru.renattele.admin95.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.form.AdminForm;

@RequestMapping("/admin/admins")
public interface AdminApi {
    @GetMapping
    String admins(Model model);

    @PostMapping
    @ResponseBody
    void createAdmin(@RequestBody AdminForm form);

    @PutMapping
    @ResponseBody
    void updateAdmin(@RequestBody AdminForm form);
}
