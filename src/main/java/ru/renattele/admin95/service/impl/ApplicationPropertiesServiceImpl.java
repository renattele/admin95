package ru.renattele.admin95.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.model.ApplicationPropertyEntity;
import ru.renattele.admin95.repository.ApplicationPropertyRepository;
import ru.renattele.admin95.service.ApplicationPropertyService;

@Slf4j
@Service("applicationPropertiesService")
@RequiredArgsConstructor
public class ApplicationPropertiesServiceImpl implements ApplicationPropertyService {
    private final ApplicationPropertyRepository applicationPropertyRepository;
    private final ObjectMapper objectMapper;

    @Override
    public <T> T get(String key, Class<T> clazz, T defaultValue) {
        return applicationPropertyRepository.findById(key)
                .map(entity -> {
                    try {
                        return objectMapper.readValue(entity.getValue(), clazz);
                    } catch (Exception e) {
                        log.error("Failed to deserialize", e);
                        return defaultValue;
                    }
                })
                .orElse(defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return get(key, String.class, defaultValue);
    }

    @Override
    public <T> void set(String key, T value) {
        var stringValue = objectMapper.valueToTree(value).toString();
        applicationPropertyRepository.save(new ApplicationPropertyEntity(key, stringValue));
    }

    @Override
    public void delete(String key) {
        applicationPropertyRepository.deleteById(key);
    }
}
