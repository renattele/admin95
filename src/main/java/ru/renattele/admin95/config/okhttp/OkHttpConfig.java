package ru.renattele.admin95.config.okhttp;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.renattele.admin95.util.UnixDomainSocketFactory;

import java.io.File;

@Configuration
public class OkHttpConfig {

    @Bean
    @OkHttpDefaultClient
    @Primary
    public OkHttpClient defaultOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    @OkHttpDockerClient
    public OkHttpClient dockerOkHttpClient(@Value("${DOCKER_SOCKET_PATH}") String dockerPath) {
        var dockerSockFile = new File(dockerPath);
        if (!dockerSockFile.exists()) {
            throw new IllegalArgumentException("Docker path not found, provided: " + dockerPath);
        }
        return new OkHttpClient.Builder()
                .socketFactory(new UnixDomainSocketFactory(dockerSockFile))
                .build();
    }
}
