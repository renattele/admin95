package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.form.BackupDeleteQueryForm;

@RequestMapping("/admin/backups")
@Tag(name = "Backup Management", description = "Operations for managing system backups")
@PreAuthorize("hasRole('ROLE_ACCESS_BACKUPS')")
public interface BackupMvcApi {
    @GetMapping
    String backups(Model model);

    @GetMapping(value = "/{id}/{name}")
    @ResponseBody
    ResponseEntity<Resource> downloadBackup(
            @Parameter(description = "Backup ID") @PathVariable("id") Long id,
            @Parameter(description = "Backup filename") @PathVariable("name") String name);

    @PostMapping(value = "/delete-count-query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    int getDeleteCount(
            @Parameter(description = "Deletion criteria form") @RequestBody BackupDeleteQueryForm form);

    @PostMapping
    @ResponseBody
    void createBackup();

    @DeleteMapping("/{id}")
    @ResponseBody
    void deleteBackup(
            @Parameter(description = "ID of the backup to delete") @PathVariable("id") Long id);

    @DeleteMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    void deleteBackupByQuery(
            @Parameter(description = "Query criteria for backups to delete") @RequestBody BackupDeleteQueryForm form);
}
