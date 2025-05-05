package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemMetricsDto {
    private Long id;

    private double cpuTemperature;

    private double cpuUsage;

    private double ramUsage;

    private double diskUsage;

    private LocalDateTime timestamp;
}
