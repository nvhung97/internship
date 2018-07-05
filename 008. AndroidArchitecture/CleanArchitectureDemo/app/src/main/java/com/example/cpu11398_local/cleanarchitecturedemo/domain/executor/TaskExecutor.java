package com.example.cpu11398_local.cleanarchitecturedemo.domain.executor;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor implements Executor{

    private final int       CORE_POOL_SIZE    = 3;
    private final int       MAX_POOL_SIZE     = 5;
    private final int       KEEP_ALIVE_TIME   = 10;
    private final TimeUnit  TIME_UNIT         = TimeUnit.SECONDS;

    private ThreadPoolExecutor threadPoolExecutor;

    public TaskExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TIME_UNIT,
                new LinkedBlockingQueue<>()
        );
    }

    @Override
    public void execute(@NonNull Runnable command) {
        threadPoolExecutor.execute(command);
    }
}
