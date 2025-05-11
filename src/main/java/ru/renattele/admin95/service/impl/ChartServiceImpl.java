package ru.renattele.admin95.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.repository.QuickChartRepository;
import ru.renattele.admin95.repository.quickchart.QuickChartCreateChartRequest;
import ru.renattele.admin95.service.ChartService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChartServiceImpl implements ChartService {
    private final QuickChartRepository quickChartRepository;

    @Override
    public String createChartUrl(List<Integer> data, List<String> labels) {
        var call = quickChartRepository.createChart(
                QuickChartCreateChartRequest.fromDatasetAndLabels(data, labels)
        );
        try {
            var response = call.execute();
            var chartResponse = response.body();
            if (chartResponse == null) {
                log.error("Error creating chart: response body is null");
                return null;
            }
            return chartResponse.url();
        } catch (IOException e) {
            log.error("Error creating chart: {}", e.getMessage());
            return null;
        }
    }
}
