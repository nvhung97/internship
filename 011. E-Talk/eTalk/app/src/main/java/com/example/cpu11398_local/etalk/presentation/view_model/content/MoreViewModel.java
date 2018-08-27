package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class MoreViewModel implements ViewModel{

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * Listener from viewModel parent.
     */
    private ViewModelCallback viewModelCallback;

    /**
     * Create new {@code MoreViewModel} with a {@code Context} and a {@code ViewModelCallback}.
     */
    public MoreViewModel(Context context, ViewModelCallback viewModelCallback) {
        this.context            = context;
        this.viewModelCallback  = viewModelCallback;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
    }

    /**
     * Called when user press my profile button.
     * @param view
     */
    public void onMyProfile(View view) {
        publisher.onNext(Event.create(Event.MORE_FRAGMENT_MY_PRORILE));
    }

    /**
     * Called when user press people nearby button.
     * @param view
     */
    public void onPeopleNearby(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press chat rooms button.
     * @param view
     */
    public void onChatRooms(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press Shop button.
     * @param view
     */
    public void onShop(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press game button.
     * @param view
     */
    public void onGame(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press channel button.
     * @param view
     */
    public void onChannel(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press taxi button.
     * @param view
     */
    public void onTaxi(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press logout button.
     * @param view
     */
    public void onLogout(View view) {
        viewModelCallback.onNewEvent(Event.create(Event.MORE_FRAGMENT_LOGOUT));
    }

    @Override
    public void endTask() {

    }
}
