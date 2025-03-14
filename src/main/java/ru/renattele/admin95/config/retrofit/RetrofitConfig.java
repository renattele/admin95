package ru.renattele.admin95.config.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.renattele.admin95.config.okhttp.OkHttpDockerClient;

@Configuration
public class RetrofitConfig {

    @Bean
    @RetrofitDefaultClient
    @Primary
    public Retrofit defaultRetrofit(ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    @Bean
    @RetrofitDockerClient
    public Retrofit dockerRetrofit(@OkHttpDockerClient OkHttpClient client, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("http://localhost")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }
}
