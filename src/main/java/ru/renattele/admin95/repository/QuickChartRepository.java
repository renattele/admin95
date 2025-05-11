package ru.renattele.admin95.repository;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.renattele.admin95.repository.quickchart.QuickChartCreateChartRequest;
import ru.renattele.admin95.repository.quickchart.QuickChartCreateChartResponse;

public interface QuickChartRepository {
    @POST("/chart/create")
    Call<QuickChartCreateChartResponse> createChart(@Body QuickChartCreateChartRequest request);
}
