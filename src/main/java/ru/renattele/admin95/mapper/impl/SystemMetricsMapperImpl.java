package ru.renattele.admin95.mapper.impl;

import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.SystemMetricsDto;
import ru.renattele.admin95.mapper.SystemMetricsMapper;
import ru.renattele.admin95.model.SystemMetricsEntity;

@Service
public class SystemMetricsMapperImpl implements SystemMetricsMapper {
    @Override
    public SystemMetricsDto toDto(SystemMetricsEntity entity) {
        return SystemMetricsDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .cpuUsage(entity.getCpuUsage())
                .cpuTemperature(entity.getCpuTemperature())
                .diskUsage(entity.getDiskUsage())
                .ramUsage(entity.getRamUsage())
                .build();
    }

    @Override
    public SystemMetricsEntity toEntity(SystemMetricsDto dto) {
        return SystemMetricsEntity.builder()
                .id(dto.getId())
                .cpuTemperature(dto.getCpuTemperature())
                .ramUsage(dto.getRamUsage())
                .diskUsage(dto.getDiskUsage())
                .cpuUsage(dto.getCpuUsage())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
