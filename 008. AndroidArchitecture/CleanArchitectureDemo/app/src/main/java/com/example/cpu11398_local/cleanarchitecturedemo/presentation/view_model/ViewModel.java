package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import io.reactivex.Observer;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public interface ViewModel<T> {
    void subscribeObserver(Observer<T> observer);
    void endTask();
}
