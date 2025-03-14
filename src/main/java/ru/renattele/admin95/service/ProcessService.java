package ru.renattele.admin95.service;

import java.io.IOException;

public interface ProcessService {
    Process runOnce(String program, String... args) throws IOException;
    Integer runIndefinitely(String program, String... args);
    void stop(Integer pid);
}
