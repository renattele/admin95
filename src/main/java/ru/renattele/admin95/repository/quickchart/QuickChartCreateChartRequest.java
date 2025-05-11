package ru.renattele.admin95.repository.quickchart;

import lombok.Builder;

import java.util.List;

@Builder
public record QuickChartCreateChartRequest(
        Chart chart
) {
    public static QuickChartCreateChartRequest fromDatasetAndLabels(List<Integer> dataset,
                                                                    List<String> labels) {
        return QuickChartCreateChartRequest.builder()
                .chart(Chart.builder()
                        .type("line")
                        .data(ChartData.builder()
                                .datasets(List.of(
                                                Dataset.builder()
                                                        .lineTension(0.4)
                                                        .pointRadius(3)
                                                        .data(dataset)
                                                        .borderColor("#4E79A7")
                                                        .backgroundColor("#4E79A733")
                                                        .borderWidth(3)
                                                        .build()
                                        )
                                )
                                .labels(labels)
                                .build())
                        .options(ChartOptions.builder()
                                .legend(Legend.builder()
                                        .display(false)
                                        .build())
                                .scales(Scales.builder()
                                        .xAxes(List.of(
                                                Axis.builder()
                                                        .ticks(Ticks.builder()
                                                                .display(true)
                                                                .fontSize(17)
                                                                .build())
                                                        .build()
                                        ))
                                        .yAxes(List.of(
                                                Axis.builder()
                                                        .ticks(Ticks.builder()
                                                                .display(true)
                                                                .fontSize(17)
                                                                .build())
                                                        .build()
                                        ))
                                        .build())
                                .build())
                        .build())
                .build();
    }

    @Builder
    public record Chart(
            String type,
            ChartData data,
            ChartOptions options
    ) {
    }

    @Builder
    public record ChartData(
            List<Dataset> datasets,
            List<String> labels
    ) {
    }

    @Builder
    public record Dataset(
            double lineTension,
            int pointRadius,
            List<Integer> data,
            String borderColor,
            String backgroundColor,
            int borderWidth
    ) {
    }

    @Builder
    public record ChartOptions(
            Legend legend,
            Scales scales
    ) {
    }

    @Builder
    public record Legend(
            boolean display
    ) {
    }

    @Builder
    public record Scales(
            List<Axis> xAxes,
            List<Axis> yAxes
    ) {
    }

    @Builder
    public record Axis(
            Ticks ticks
    ) {
    }

    @Builder
    public record Ticks(
            boolean display,
            int fontSize
    ) {
    }
}