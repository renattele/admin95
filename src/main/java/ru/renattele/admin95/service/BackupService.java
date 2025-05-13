package ru.renattele.admin95.service;

import org.jetbrains.annotations.Nullable;
import ru.renattele.admin95.dto.BackupDeleteQueryDto;
import ru.renattele.admin95.dto.BackupDto;

import java.io.InputStream;
import java.util.List;

public interface BackupService {
    List<BackupDto> getAll();

    @Nullable BackupDto getById(Long id);

    InputStream getBackupStream(BackupDto backupDto);

    void runBackupInBackground();

    void deleteBackup(BackupDto backupDto);

    List<BackupDto> getBackupsMatching(BackupDeleteQueryDto query);

    void deleteBackupsMatching(BackupDeleteQueryDto query);
}
