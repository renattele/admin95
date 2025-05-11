package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.DashboardApi;
import ru.renattele.admin95.model.SystemMetricsEntity;
import ru.renattele.admin95.service.ChartService;
import ru.renattele.admin95.service.SystemMetricsService;
import ru.renattele.admin95.util.ConcurrentUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

@Controller
@RequiredArgsConstructor
public class DashboardController implements DashboardApi {
    private final SystemMetricsService systemMetricsService;

    private final ChartService chartService;

    @Value("${dashboard.displayed-hours}")
    private int displayedHours;

    @Override
    public String dashboard(Model model) {
        var endTime = LocalDateTime.now();
        var startTime = endTime.minusHours(displayedHours);
        var metrics = systemMetricsService.getSystemMetricsHourly(startTime, endTime);
        var urls = ConcurrentUtil.asyncList(
                () -> getUrlFor(metrics, SystemMetricsEntity::getCpuUsage),
                () -> getUrlFor(metrics, SystemMetricsEntity::getCpuTemperature),
                () -> getUrlFor(metrics, SystemMetricsEntity::getRamUsage),
                () -> getUrlFor(metrics, SystemMetricsEntity::getDiskUsage)
        );
        model.addAttribute("CPU_USAGE_URL", urls.get(0));
        model.addAttribute("CPU_TEMPERATURE_URL", urls.get(1));
        model.addAttribute("RAM_URL", urls.get(2));
        model.addAttribute("DISK_URL", urls.get(3));
        return "dashboard";
    }

    private <T> List<T> getSpecificQuery(
            List<SystemMetricsEntity> metrics,
            Function<SystemMetricsEntity, T> function) {
        return metrics.stream()
                .map(function)
                .toList();
    }

    private String getUrlFor(List<SystemMetricsEntity> metrics, ToDoubleFunction<SystemMetricsEntity> function) {
        var dataQuery = getSpecificQuery(metrics, e -> (int) function.applyAsDouble(e));

        var dateFormatter = DateTimeFormatter.ofPattern("HH:mm");
        var labelsQuery = getSpecificQuery(metrics, e -> e.getTimestamp().format(dateFormatter));
        return chartService.createChartUrl(
                dataQuery, labelsQuery
        );
    }
}
