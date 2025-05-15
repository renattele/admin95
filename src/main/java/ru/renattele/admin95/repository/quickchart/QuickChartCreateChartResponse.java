package ru.renattele.admin95.repository.quickchart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuickChartCreateChartResponse(String url) {
}
