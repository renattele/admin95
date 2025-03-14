package ru.renattele.admin95.repository.docker;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface DockerApiRepository {
    @GET("/containers/json")
    Call<List<DockerGetContainersResponse>> containers();

    @GET("/containers/{id}/logs")
    Call<ResponseBody> containerLogs(@Path("id") String id, @Query("stdout") boolean stdout, @Query("stderr") boolean stderr);
}
