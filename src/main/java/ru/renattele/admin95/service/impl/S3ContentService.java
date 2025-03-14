package ru.renattele.admin95.service.impl;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.service.ContentService;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class S3ContentService implements ContentService {
    private MinioClient client;

    @Value("${S3_ENDPOINT}")
    private String endpoint;

    @Value("${S3_ACCESS_KEY}")
    private String accessKey;

    @Value("${S3_SECRET_KEY}")
    private String secretKey;

    @Value("${S3_BUCKET}")
    private String bucket;

    @PostConstruct
    public void init() {
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        try {
            var found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Optional<InputStream> get(UUID id) {
        try {
            var response = client.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(id.toString())
                            .build()
            );
            return Optional.of(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UUID> put(InputStream inputStream) {
        try {
            var id = UUID.randomUUID();
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(id.toString())
                            .stream(inputStream, inputStream.available(), -1)
                            .build()
            );
            return Optional.of(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(id.toString())
                            .build()
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
