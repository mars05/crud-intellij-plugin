package com.github.mars05.crud.intellij.plugin.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyu
 */
public class ThreadUtils {
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
