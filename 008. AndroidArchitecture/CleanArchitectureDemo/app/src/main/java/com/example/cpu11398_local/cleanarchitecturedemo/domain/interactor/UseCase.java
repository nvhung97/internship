package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

/**
 * Created by Hung-pc on 7/8/2018.
 */

public interface UseCase {
    void execute(Object observer, Object params);
    void endTask();
}
