package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Component;
import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.mapper.BackupMapper;
import ru.renattele.admin95.model.BackupEntity;

@Component
public class BackupMapperImpl implements BackupMapper {
    @Override
    public BackupDto toDto(BackupEntity backupEntity) {
        return BackupDto.builder()
                .id(backupEntity.getId())
                .name(backupEntity.getName())
                .size(backupEntity.getSize())
                .timestamp(backupEntity.getTimestamp())
                .running(backupEntity.getRunning())
                .message(backupEntity.getMessage())
                .build();
    }

    @Override
    public BackupEntity toEntity(BackupDto backupDto) {
        return BackupEntity.builder()
                .id(backupDto.getId())
                .name(backupDto.getName())
                .size(backupDto.getSize())
                .timestamp(backupDto.getTimestamp())
                .running(backupDto.getRunning())
                .message(backupDto.getMessage())
                .build();
    }
}
