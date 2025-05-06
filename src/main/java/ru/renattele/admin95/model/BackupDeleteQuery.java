package ru.renattele.admin95.model;

import lombok.Builder;

import java.time.temporal.ChronoUnit;

@Builder
public
record BackupDeleteQuery(
        Long ageAmount,
        ChronoUnit ageUnit,
        Long minSize,
        Long maxSize
) {
}
