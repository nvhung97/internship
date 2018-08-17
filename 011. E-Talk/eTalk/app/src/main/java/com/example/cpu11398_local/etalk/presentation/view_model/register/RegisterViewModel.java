package com.example.cpu11398_local.etalk.presentation.view_model.register;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class RegisterViewModel implements ViewModel, NetworkChangeReceiver.NetworkChangeListener{

    /**
     * Binding data between {@code fullname} and Fullname {@code EditText} on view.
     */
    public ObservableField<String> fullname = new ObservableField<>("");

    /**
     * Binding data between {@code username} and Username {@code EditText} on view.
     */
    public ObservableField<String> username = new ObservableField<>("");

    /**
     * Binding data between {@code password} and Password {@code EditText} on view.
     */
    public ObservableField<String> password = new ObservableField<>("");

    /**
     * Binding data between {@code phoneNumber} and Phone number {@code EditText} on view.
     */
    public ObservableField<String> phoneNumber = new ObservableField<>("");

    /**
     * Binding data between {@code visiblePassword} and attribute {@code inputType} of
     * password {@code EditText}.
     */
    public ObservableBoolean visiblePassword = new ObservableBoolean(false);

    /**
     * Binding data between {@code hintEvent} and attribute {@code hintWithEvent} of
     * {@code EditText} input hint on view.
     */
    public ObservableField<Event> hintEvent = new ObservableField<>(Event.create(Event.NONE));

    /**
     * Binding data between {@code isNetworkAvailable} and {@code TextView} for inform
     * state of network.
     */
    public ObservableBoolean isNetworkAvailable = new ObservableBoolean(false);

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * When user request register, viewModel use {@code registerUsecase} to perform the action.
     */
    private Usecase registerUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * create new {@code RegisterViewModel} with a context, an usecase and a receiver to listen
     * network state.
     */
    @Inject
    public RegisterViewModel(Context context,
                             @Named("RegisterUsecase") Usecase registerUsecase,
                             NetworkChangeReceiver receiver) {
        this.context            = context;
        this.registerUsecase    = registerUsecase;
        this.receiver           = receiver;
        receiver.initReceiver(this.context, this);
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
        publisher.onNext(Event.create(Event.REGISTER_ACTIVITY_BACK));
    }

    /**
     * Called when user click register button.
     * @param view
     */
    public void onRegisterRequest(View view) {
        publisher.onNext(Event.create(Event.REGISTER_ACTIVITY_SHOW_LOADING));
        hintEvent.set(Event.create(Event.NONE));
        registerUsecase.execute(
                new RegisterObserver(),
                fullname.get(),
                username.get(),
                password.get(),
                phoneNumber.get()
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
        registerUsecase.endTask();
        receiver.endReceiver();
    }

    /**
     * Called when network state change an reassign {@code isNetworkAvailable}
     * according to {@code networkState}.
     * @param networkState current network state.
     */
    @Override
    public void onNetworkChange(boolean networkState) {
        isNetworkAvailable.set(networkState);
    }

    /**
     * {@code LoginObserver} is subscribed to usecase to listen event from it.
     */
    private class RegisterObserver extends DisposableSingleObserver<Boolean> {

        private Handler handler = new Handler();

        public RegisterObserver() {
            handler.postDelayed(
                    () -> {
                        hintEvent.set(Event.create(Event.REGISTER_ACTIVITY_TIMEOUT));
                        publisher.onNext(Event.create(Event.REGISTER_ACTIVITY_HIDE_LOADING));
                        registerUsecase.endTask();
                    },
                    1000 * 30
            );
        }

        @Override
        public void onSuccess(Boolean isSuccess) {
            handler.removeCallbacksAndMessages(null);
            if (isSuccess) {
                publisher.onNext(Event.create(Event.REGISTER_ACTIVITY_FINISH_OK));
            } else {
                publisher.onNext(Event.create(Event.REGISTER_ACTIVITY_HIDE_LOADING));
                hintEvent.set(Event.create(Event.REGISTER_ACTIVITY_REGISTER_FAILED));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }
}
