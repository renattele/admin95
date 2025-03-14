package ru.renattele.admin95.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.renattele.admin95.config.retrofit.RetrofitDockerClient;
import ru.renattele.admin95.model.docker.ContainerEntity;
import ru.renattele.admin95.repository.DockerRepository;
import ru.renattele.admin95.repository.docker.DockerApiRepository;
import ru.renattele.admin95.repository.docker.DockerGetContainersResponse;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DockerRepositoryImpl implements DockerRepository {
    private final DockerApiRepository dockerApiRepository;

    public DockerRepositoryImpl(
            @RetrofitDockerClient Retrofit retrofit
    ) {
        dockerApiRepository = retrofit.create(DockerApiRepository.class);
    }

    @Override
    public String logsFor(ContainerEntity container) {
        try {
            var response = dockerApiRepository.containerLogs(container.getId(), true, true).execute();
            try (var body = response.body()) {
                if (body == null) {
                    log.error("Can't fetch logs for container {}, response body is null", container.getId());
                    return "";
                }
                return convertLogsToString(body.bytes());
            }
        } catch (IOException e) {
            log.error("Can't fetch logs for container {}", container.getId(), e);
            return "";
        }
    }

    private String convertLogsToString(byte[] bytes) {
        StringBuilder logs = new StringBuilder();
        int index = 0;

        while (index < bytes.length) {
            index += 8;

            int length = ((bytes[index - 4] & 0xFF) << 24) |
                    ((bytes[index - 3] & 0xFF) << 16) |
                    ((bytes[index - 2] & 0xFF) << 8) |
                    (bytes[index - 1] & 0xFF);

            String logEntry = new String(bytes, index, length);
            logs.append(logEntry);

            index += length;
        }

        return logs.toString();
    }

    @Override
    public List<ContainerEntity> containers() {
        try {
            var response = dockerApiRepository.containers().execute();
            var body = response.body();
            if (body == null) {
                log.error("Can't fetch containers, response body is null");
                return List.of();
            }
            return body
                    .stream()
                    .map(DockerGetContainersResponse::toEntity)
                    .toList();
        } catch (IOException e) {
            log.error("Can't fetch containers", e);
            return List.of();
        }
    }
}
