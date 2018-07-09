package com.example.cpu11398_local.cleanarchitecturedemo.domain.executor;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor implements Executor{

    private ThreadPoolExecutor threadPoolExecutor;

    public TaskExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                3,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    @Override
    public void execute(@NonNull Runnable command) {
        threadPoolExecutor.execute(command);
    }
}
