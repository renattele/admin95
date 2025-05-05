package ru.renattele.admin95.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class ProcessUtil {
    private ProcessUtil() {}
    public static ProcessBuilder builder(List<String> initialArgs, String... args) {
        var processArgs = Stream.concat(
                initialArgs.stream(),
                Arrays.stream(args)).toList();
        return new ProcessBuilder(processArgs);
    }

    public static String readInputStream(Process process) {
        try {
            return IOUtil.readFromReader(process.inputReader());
        } catch (IOException e) {
            log.error("Cannot read process input stream", e);
            return "";
        }
    }

    public static String readErrorStream(Process process) {
        try {
            return IOUtil.readFromReader(process.errorReader());
        } catch (IOException e) {
            log.error("Cannot read process error stream", e);
            return "";
        }
    }
}
