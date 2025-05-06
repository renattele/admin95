package ru.renattele.admin95.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileSizeUnit {
    BYTE(1, "file.size.unit.byte"),
    KILOBYTE(BYTE.multiplier * 1024, "file.size.unit.kilobyte"),
    MEGABYTE(KILOBYTE.multiplier * 1024, "file.size.unit.megabyte"),
    GIGABYTE(MEGABYTE.multiplier * 1024, "file.size.unit.gigabyte"),
    TERABYTE(GIGABYTE.multiplier * 1024L, "file.size.unit.terabyte"),
    ;
    private final long multiplier;
    private final String messageKey;
}
