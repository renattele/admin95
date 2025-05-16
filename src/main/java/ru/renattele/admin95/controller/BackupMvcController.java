package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.BackupApi;
import ru.renattele.admin95.api.BackupMvcApi;
import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.form.BackupDeleteQueryForm;
import ru.renattele.admin95.service.BackupService;

import java.util.Comparator;

@Controller
@RequiredArgsConstructor
public class BackupMvcController implements BackupMvcApi {
    private final BackupService backupService;
    private final BackupApi backupApi;

    @Override
    public String backups(Model model) {
        var backups = backupService.getAll();
        var sortedBackups = backups.stream()
                .sorted(Comparator.comparing(BackupDto::getTimestamp).reversed())
                .toList();
        model.addAttribute("backups", sortedBackups);
        return "backups";
    }

    @Override
    public ResponseEntity<Resource> downloadBackup(Long id, String name) {
        return backupApi.downloadBackup(id, name);
    }

    @Override
    public int getDeleteCount(BackupDeleteQueryForm form) {
        return backupApi.getDeleteCount(form);
    }

    @Override
    public void createBackup() {
        backupApi.createBackup();
    }

    @Override
    public void deleteBackup(Long id) {
        backupApi.deleteBackup(id);
    }

    @Override
    public void deleteBackupByQuery(BackupDeleteQueryForm form) {
        backupApi.deleteBackupByQuery(form);
    }
}
