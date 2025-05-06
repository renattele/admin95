package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.model.BackupEntity;

public interface BackupMapper {
    BackupDto toDto(BackupEntity backupEntity);

    BackupEntity toEntity(BackupDto backupDto);
}
