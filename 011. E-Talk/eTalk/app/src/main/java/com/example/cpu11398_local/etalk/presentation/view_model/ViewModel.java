package com.example.cpu11398_local.etalk.presentation.view_model;

import com.example.cpu11398_local.etalk.utils.Event;
import io.reactivex.Observer;

public interface ViewModel {

    /**
     * View subscribe an observer to ViewModel to listen event emitted from it.
     * @param observer listen event from ViewModel
     */
    void subscribeObserver(Observer<Event> observer);

    /**
     *  View called this method to inform ViewModel stop usecase that in progress
     *  when it finish.
     */
    void endTask();
}
