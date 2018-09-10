package com.example.cpu11398_local.etalk.presentation.view_model;

import com.example.cpu11398_local.etalk.utils.Event;

import io.reactivex.Observer;

public interface ViewModelCallback {

    int MESSAGES    = 0;
    int CONTACTS    = 1;
    int GROUPS      = 2;
    int TIMELINE    = 3;
    int MORE        = 4;


    /**
     * Child viewModel subscribe an observer to parent viewModel to listen event emitted from it.
     * @param observer listen event from parent viewModel.
     */
    void onChildViewModelSubscribeObserver(Observer<Event> observer, int code);

    /**
     * Used when child viewModel want it's parent viewModel do something.
     * @param event event that emitted to viewModel Parent.
     */
    void onHelp(Event event);
}
