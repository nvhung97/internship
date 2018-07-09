package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by Hung-pc on 7/8/2018.
 */

public interface UseCase<T, P> {
    void execute(DisposableObserver<T> observer, P params);
    void execute(DisposableSingleObserver<T> observer, P params);
    void execute(DisposableCompletableObserver observer, P params);
    void endTask();
}
