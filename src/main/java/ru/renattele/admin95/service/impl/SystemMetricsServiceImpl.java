package ru.renattele.admin95.service.impl;

import com.sun.management.OperatingSystemMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.model.SystemMetricsEntity;
import ru.renattele.admin95.repository.SystemMetricsRepository;
import ru.renattele.admin95.service.SystemMetricsService;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemMetricsServiceImpl implements SystemMetricsService {
    private final SystemMetricsRepository metricsRepository;
    @Value("${dashboard.cpu-temp-path}")
    private String cpuTempFilePath;

    @Override
    public List<SystemMetricsEntity> getSystemMetrics(
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return metricsRepository.findMetricsInTimeRange(startTime, endTime);
    }

    @Override
    public List<SystemMetricsEntity> getSystemMetricsHourly(LocalDateTime startTime, LocalDateTime endTime) {
        return getSystemMetrics(startTime, endTime).stream()
                .collect(Collectors.groupingBy(metrics ->
                        metrics.getTimestamp().getHour()
                ))
                .values().stream()
                .map(List::getFirst)
                .sorted(Comparator.comparing(SystemMetricsEntity::getTimestamp))
                .toList();
    }


    @Scheduled(fixedRateString = "${dashboard.collect-millis}")
    @Override
    public void collectMetrics() {
        try {
            SystemMetricsEntity metrics = new SystemMetricsEntity();
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            metrics.setCpuUsage(osBean.getCpuLoad() * 100);
            metrics.setRamUsage(getRamUsage());
            metrics.setDiskUsage(getDiskUsage());
            metrics.setCpuTemperature(getCpuTemperature());
            metrics.setTimestamp(LocalDateTime.now());

            metricsRepository.save(metrics);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private double getRamUsage() {
        var runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return ((double) (totalMemory - freeMemory) / totalMemory) * 100;
    }

    private double getDiskUsage() {
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        return ((double) (totalSpace - freeSpace) / totalSpace) * 100;
    }

    private double getCpuTemperature() throws IOException {
        File temp = new File(cpuTempFilePath);
        String temperature = new String(java.nio.file.Files.readAllBytes(temp.toPath()));
        return Double.parseDouble(temperature.trim()) / 1000.0;
    }
}
