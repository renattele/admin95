package ru.renattele.admin95.service;

public interface ApplicationPropertyService {
    <T> T get(String key, Class<T> clazz, T defaultValue);

    String getString(String key, String defaultValue);

    <T> void set(String key, T value);

    void delete(String key);
}
