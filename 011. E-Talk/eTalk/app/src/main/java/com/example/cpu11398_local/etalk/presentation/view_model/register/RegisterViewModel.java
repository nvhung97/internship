package com.example.cpu11398_local.etalk.presentation.view_model.register;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class RegisterViewModel implements ViewModel {

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
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * create new {@code RegisterViewModel} with a context.
     */
    @Inject
    public RegisterViewModel(Context context) {
        this.context = context;
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
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click eye button to show or hide password.
     * @param view
     */
    public void onChangeVisiblePasswordState(View view) {
        visiblePassword.set(!visiblePassword.get());
    }

    @Override
    public void endTask() {

    }
}
