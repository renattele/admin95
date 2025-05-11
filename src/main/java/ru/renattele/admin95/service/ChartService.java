package ru.renattele.admin95.service;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ChartService {
    /**
     * Returns url for chart with data and labels
     */
    @Nullable String createChartUrl(List<Integer> data, List<String> labels);
}
