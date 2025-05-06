package ru.renattele.admin95.service;

import org.jetbrains.annotations.Nullable;
import ru.renattele.admin95.dto.BackupDeleteQueryDto;
import ru.renattele.admin95.dto.BackupDto;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BackupService {
    List<BackupDto> getAll();

    @Nullable BackupDto getById(Long id);

    InputStream getBackupStream(BackupDto backupDto);

    CompletableFuture<Void> runBackupInBackground();

    void deleteBackup(BackupDto backupDto);

    List<BackupDto> getBackupsMatching(BackupDeleteQueryDto query);

    void deleteBackupsMatching(BackupDeleteQueryDto query);
}
