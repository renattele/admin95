package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.renattele.admin95.api.DashboardApi;
import ru.renattele.admin95.model.SystemMetricsEntity;
import ru.renattele.admin95.service.SystemMetricsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController implements DashboardApi {
    private final SystemMetricsService systemMetricsService;

    @Value("${dashboard.base-url}")
    private String baseUrl;

    @Value("${dashboard.displayed-hours}")
    private int displayedHours;

    @Override
    public String dashboard(Model model) {
        var endTime = LocalDateTime.now();
        var startTime = endTime.minusHours(displayedHours);
        var metrics = systemMetricsService.getSystemMetricsHourly(startTime, endTime);
        model.addAttribute("CPU_USAGE_URL", getUrlFor(metrics, SystemMetricsEntity::getCpuUsage));
        model.addAttribute("CPU_TEMPERATURE_URL", getUrlFor(metrics, SystemMetricsEntity::getCpuTemperature));
        model.addAttribute("RAM_URL", getUrlFor(metrics, SystemMetricsEntity::getRamUsage));
        model.addAttribute("DISK_URL", getUrlFor(metrics, SystemMetricsEntity::getDiskUsage));
        return "dashboard";
    }

    private String getSpecificQuery(
            List<SystemMetricsEntity> metrics,
            Function<SystemMetricsEntity, Object> function) {
        return metrics.stream()
                .map(function)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    private String getUrlFor(List<SystemMetricsEntity> metrics, ToDoubleFunction<SystemMetricsEntity> function) {
        var dataQuery = getSpecificQuery(metrics, e -> (int) function.applyAsDouble(e));

        var dateFormatter = DateTimeFormatter.ofPattern("HH:mm");
        var labelsQuery = getSpecificQuery(metrics, e -> e.getTimestamp().format(dateFormatter));
        return baseUrl.replace("{data}", dataQuery)
                .replace("{labels}", labelsQuery);
    }
}
