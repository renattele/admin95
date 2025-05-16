package ru.renattele.admin95.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

public class IOUtil {
    public static String readFromReader(Reader reader) throws IOException {
        try (var bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }
}
