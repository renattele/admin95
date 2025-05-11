package ru.renattele.admin95.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class ConcurrentUtil {
    private ConcurrentUtil() {
    }

    public static <T> List<T> asyncList(Supplier<T>... suppliers) {
        var futures = Arrays.stream(suppliers)
                .map(CompletableFuture::supplyAsync)
                .toList();

        var allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        var allResults = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .toList()
        );
        try {
            return allResults.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error while executing async tasks", e);
        }
    }
}
