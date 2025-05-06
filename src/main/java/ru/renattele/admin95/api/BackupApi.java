package ru.renattele.admin95.api;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.forms.BackupDeleteQueryForm;

@RequestMapping("/admin/backups")
public interface BackupApi {
    @GetMapping
    String backups(Model model);

    @GetMapping(value = "/{id}/{name}")
    @ResponseBody
    ResponseEntity<Resource> downloadBackup(@PathVariable("id") Long id, @PathVariable("name") String name);

    @PostMapping(value = "/delete-count-query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    int getDeleteCount(@RequestBody BackupDeleteQueryForm form);

    @PostMapping
    @ResponseBody
    void createBackup();

    @DeleteMapping("/{id}")
    @ResponseBody
    void deleteBackup(@PathVariable("id") Long id);

    @DeleteMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    void deleteBackupByQuery(@RequestBody BackupDeleteQueryForm form);
}
