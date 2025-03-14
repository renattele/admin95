package ru.renattele.admin95.service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

public interface ContentService {
    Optional<InputStream> get(UUID id);
    Optional<UUID> put(InputStream content);
    boolean delete(UUID id);
}
