package ru.renattele.admin95.service;

import ru.renattele.admin95.model.SystemMetricsEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface SystemMetricsService {
    List<SystemMetricsEntity> getSystemMetrics(
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    List<SystemMetricsEntity> getSystemMetricsHourly(
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    void collectMetrics();
}