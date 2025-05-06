package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Component;
import ru.renattele.admin95.dto.BackupDeleteQueryDto;
import ru.renattele.admin95.form.BackupDeleteQueryForm;
import ru.renattele.admin95.mapper.BackupDeleteQueryMapper;
import ru.renattele.admin95.model.BackupDeleteQuery;
import ru.renattele.admin95.model.BackupRetentionPeriod;
import ru.renattele.admin95.model.FileSizeUnit;

import java.time.temporal.ChronoUnit;

@Component
public class BackupDeleteQueryMapperImpl implements BackupDeleteQueryMapper {
    @Override
    public BackupDeleteQueryDto toDto(BackupDeleteQuery backupDeleteQuery) {
        return new BackupDeleteQueryDto(
                backupDeleteQuery.ageAmount(),
                backupDeleteQuery.ageUnit().name(),
                backupDeleteQuery.minSize(),
                backupDeleteQuery.maxSize()
        );
    }

    @Override
    public BackupDeleteQuery toEntity(BackupDeleteQueryDto backupDeleteQueryDto) {
        return new BackupDeleteQuery(
                backupDeleteQueryDto.getAgeAmount(),
                ChronoUnit.valueOf(backupDeleteQueryDto.getAgeUnit()),
                backupDeleteQueryDto.getMinSize(),
                backupDeleteQueryDto.getMaxSize()
        );
    }

    @Override
    public BackupDeleteQueryDto toDto(BackupDeleteQueryForm form) {
        var retentionPeriod = form.getAge() != null ? BackupRetentionPeriod.valueOf(form.getAge()) : null;
        var minSizeUnit = form.getMinSizeUnit() != null ?
                FileSizeUnit.valueOf(form.getMinSizeUnit()) :
                null;
        var maxSizeUnit = form.getMaxSizeUnit() != null ?
                FileSizeUnit.valueOf(form.getMaxSizeUnit()) :
                null;

        var minSize = form.getMinSize() != null && minSizeUnit != null ?
                form.getMinSize() * minSizeUnit.getMultiplier() :
                null;
        var maxSize = form.getMaxSize() != null && maxSizeUnit != null ?
                form.getMaxSize() * maxSizeUnit.getMultiplier() :
                null;
        return BackupDeleteQueryDto.builder()
                .ageAmount(retentionPeriod != null ? retentionPeriod.getAgeAmount() : null)
                .ageUnit(retentionPeriod != null ? retentionPeriod.getChronoUnit().name() : null)
                .minSize(minSize)
                .maxSize(maxSize)
                .build();
    }
}
