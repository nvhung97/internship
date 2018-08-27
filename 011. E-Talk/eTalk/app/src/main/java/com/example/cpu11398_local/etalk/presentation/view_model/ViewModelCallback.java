package com.example.cpu11398_local.etalk.presentation.view_model;

import com.example.cpu11398_local.etalk.utils.Event;

public interface ViewModelCallback {

    /**
     * Used when child viewModel want it's parent viewModel do something.
     * @param event event that emitted to viewModel Parent.
     */
    void onNewEvent(Event event);
}
