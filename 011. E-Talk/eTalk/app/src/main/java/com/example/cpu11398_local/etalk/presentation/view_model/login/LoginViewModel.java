package com.example.cpu11398_local.etalk.presentation.view_model.login;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel implements ViewModel {

    /**
     * Binding data between {@code username} and Username {@code EditText} on view.
     */
    public ObservableField<String> username = new ObservableField<>("");

    /**
     * Binding data between {@code password} and Password {@code EditText} on view.
     */
    public ObservableField<String> password = new ObservableField<>("");

    /**
     * Binding data between {@code visiblePassword} and attribute {@code inputType} of
     * password {@code EditText}.
     */
    public ObservableBoolean visiblePassword = new ObservableBoolean(false);

    /**
     * Binding data between {@code loginFailed} and attribute {@code text} and
     * {@code textColor} of {@code EditText} input hint on view.
     */
    public ObservableBoolean loginFailed = new ObservableBoolean(false);

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * When user request login, viewModel use {@code loginUsecase} to perform the action.
     */
    private Usecase loginUsecase;

    /**
     * create new {@code LoginViewModel} with a context and an usecase.
     */
    @Inject
    public LoginViewModel(Context context, @Named("LoginUsecase") Usecase loginUsecase) {
        this.context      = context;
        this.loginUsecase = loginUsecase;
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
     * Called when user click back arrow on Tool bar.
     * @param view
     */
    public void onBackPressed(View view) {
        publisher.onNext(Event.create(Event.LOGIN_ACTIVITY_BACK));
    }

    /**
     * Called when user click login button.
     * @param view
     */
    public void onLoginRequest(View view) {
        publisher.onNext(Event.create(Event.LOGIN_ACTIVITY_SHOW_LOADING));
        loginUsecase.execute(
                new LoginObserver(),
                username.get(),
                password.get()
        );
    }

    /**
     * Called when user click eye button to show or hide password.
     * @param view
     */
    public void onChangeVisiblePasswordState(View view) {
        visiblePassword.set(!visiblePassword.get());
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        loginUsecase.endTask();
    }

    /**
     * {@code LoginObserver} is subscribed to usecase to listen event from it.
     */
    private class LoginObserver extends DisposableSingleObserver<Boolean> {
        @Override
        public void onSuccess(Boolean isSuccess) {
            if (isSuccess) {
                publisher.onNext(Event.create(Event.LOGIN_ACTIVITY_FINISH_OK));
            } else {
                publisher.onNext(Event.create(Event.LOGIN_ACTIVITY_HIDE_LOADING));
                loginFailed.set(true);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }
}
