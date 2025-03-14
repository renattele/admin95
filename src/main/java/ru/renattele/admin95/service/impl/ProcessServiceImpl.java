package ru.renattele.admin95.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.service.ProcessService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {
    @Override
    public Process runOnce(String program, String... args) throws IOException {
        var processArgs = Stream.concat(
                Stream.of(program),
                Arrays.stream(args)).toList();
        return new ProcessBuilder()
                .command(processArgs)
                .start();
    }

    private final Map<Integer, Thread> processes = new HashMap<>();

    @Override
    public synchronized Integer runIndefinitely(String program, String... args) {
        var id = new SecureRandom().nextInt();
        var thread = new Thread(() -> {
            while (true) {
                try {
                    runOnce(program, args).waitFor();
                } catch (Exception e) {
                    log.error("Error while running process", e);
                }
            }
        });
        thread.start();
        processes.put(id, thread);
        return id;
    }

    @Override
    public void stop(Integer id) {
        var thread = processes.get(id);
        if (thread != null) {
            thread.interrupt();
        }
    }
}
