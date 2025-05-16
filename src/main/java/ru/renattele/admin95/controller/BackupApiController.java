package ru.renattele.admin95.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.renattele.admin95.api.BackupApi;
import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.form.BackupDeleteQueryForm;
import ru.renattele.admin95.mapper.BackupDeleteQueryMapper;
import ru.renattele.admin95.service.BackupService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BackupApiController implements BackupApi {
    private final BackupService backupService;
    private final BackupDeleteQueryMapper backupDeleteQueryMapper;

    @Override
    public List<BackupDto> getBackups() {
        var backups = backupService.getAll();
        return backups.stream()
                .sorted(Comparator.comparing(BackupDto::getTimestamp).reversed())
                .toList();
    }

    @Override
    public ResponseEntity<Resource> downloadBackup(Long id, String name) {
        var backup = backupService.getById(id);
        if (backup == null) throw new ResourceNotFoundException();
        if (!backup.getName().equals(name)) throw new ResourceNotFoundException();
        var resource = new InputStreamResource(backupService.getBackupStream(backup));

        var mimeType = MimeMappings.DEFAULT.get(
                backup.getName().substring(
                        backup.getName().lastIndexOf('.') + 1
                ));
        var headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", backup.getName());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(backup.getSize())
                .contentType(MediaType.parseMediaType(mimeType))
                .cacheControl(CacheControl.noCache())

                .body(resource);
    }

    @Override
    public int getDeleteCount(@Valid BackupDeleteQueryForm form) {
        var queryDto = backupDeleteQueryMapper.toDto(form);
        var matches = backupService.getBackupsMatching(queryDto);
        return matches.size();
    }

    @Override
    public void createBackup() {
        backupService.runBackupInBackground();
    }

    @Override
    public void deleteBackup(Long id) {
        var backup = backupService.getById(id);
        if (backup == null) throw new ResourceNotFoundException();
        backupService.deleteBackup(backup);
    }

    @Override
    public void deleteBackupByQuery(BackupDeleteQueryForm form) {
        var queryDto = backupDeleteQueryMapper.toDto(form);
        backupService.deleteBackupsMatching(queryDto);
    }
}
