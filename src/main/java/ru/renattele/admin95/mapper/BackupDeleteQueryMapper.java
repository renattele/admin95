package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.BackupDeleteQueryDto;
import ru.renattele.admin95.form.BackupDeleteQueryForm;
import ru.renattele.admin95.model.BackupDeleteQuery;

public interface BackupDeleteQueryMapper {
    BackupDeleteQueryDto toDto(BackupDeleteQuery backupDeleteQuery);

    BackupDeleteQuery toEntity(BackupDeleteQueryDto backupDeleteQueryDto);

    BackupDeleteQueryDto toDto(BackupDeleteQueryForm form);
}
