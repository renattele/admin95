package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackupDeleteQueryDto {
    private Long ageAmount;
    private String ageUnit;
    private Long minSize;
    private Long maxSize;
}
