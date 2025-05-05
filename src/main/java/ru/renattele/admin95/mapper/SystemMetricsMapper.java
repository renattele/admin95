package ru.renattele.admin95.mapper;

import ru.renattele.admin95.dto.SystemMetricsDto;
import ru.renattele.admin95.model.SystemMetricsEntity;

public interface SystemMetricsMapper {
    SystemMetricsDto toDto(SystemMetricsEntity entity);
    SystemMetricsEntity toEntity(SystemMetricsDto dto);
}
