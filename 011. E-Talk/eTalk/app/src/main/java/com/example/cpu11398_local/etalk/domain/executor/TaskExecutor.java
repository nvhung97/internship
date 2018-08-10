package com.example.cpu11398_local.etalk.domain.executor;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor implements Executor{

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * Create a new {@Code TaskExecutor} with a {@code ThreadPoolExecutor}.
     */
    public TaskExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                3,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    /**
     * Executes the given command at some time in the future. The command is
     * executed in a pooled thread.
     * @param command the runnable task.
     */
    @Override
    public void execute(@NonNull Runnable command) {
        threadPoolExecutor.execute(command);
    }
}