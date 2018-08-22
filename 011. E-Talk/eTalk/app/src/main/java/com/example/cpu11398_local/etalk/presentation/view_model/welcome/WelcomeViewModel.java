package com.example.cpu11398_local.etalk.presentation.view_model.welcome;

import android.content.Context;
import android.util.Log;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class WelcomeViewModel implements ViewModel {

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * When app starting, viewModel use {@code welcomeUsecase} to check if
     * user has previously logged in.
     */
    private Usecase welcomeUsecase;

    /**
     * create new {@code WelcomeViewModel} with a context and an usecase.
     */
    @Inject
    public WelcomeViewModel(Context context, @Named("WelcomeUsecase") Usecase welcomeUsecase) {
        this.context        = context;
        this.welcomeUsecase = welcomeUsecase;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        publisher.onNext(Event.create(Event.WELCOME_ACTIVITY_SHOW_LOADING));
        welcomeUsecase.execute(new WelcomeObserver(), null);
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        welcomeUsecase.endTask();
    }

    /**
     * {@code WelcomeObserver} is subscribed to usecase to listen event from it.
     */
    private class WelcomeObserver extends DisposableSingleObserver<Boolean> {

        @Override
        public void onSuccess(Boolean hasUserLoggedIn) {
            if (hasUserLoggedIn) {
                publisher.onNext(Event.create(Event.WELCOME_ACTIVITY_USER_LOGGED_IN));
            } else {
                publisher.onNext(Event.create(Event.WELCOME_ACTIVITY_HIDE_LOADING));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }
}
