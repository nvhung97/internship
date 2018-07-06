package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import android.view.View;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseRegister;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class RegisterViewModel {

    public ObservableField<String>  username          = new ObservableField<>("");
    public ObservableField<String>  password          = new ObservableField<>("");
    public ObservableField<Boolean> isUsernameEmpty   = new ObservableField<>(false);
    public ObservableField<Boolean> isPasswordEmpty   = new ObservableField<>(false);
    public PublishSubject<Boolean>  registerPublisher = PublishSubject.create();

    private UseCaseRegister         useCaseRegister;

    @Inject
    public RegisterViewModel(UseCaseRegister useCaseRegister) {
        this.useCaseRegister = useCaseRegister;
    }

    public void subscribeObserver(Observer<Boolean> observer) {
        registerPublisher.subscribe(observer);
    }

    public void performRegister(View v) {
        if (!isExistEmptyField()){
            useCaseRegister.execute(
                    new RegisterObserer(),
                    new User(
                            username.get(),
                            password.get()
                    )
            );
        }
    }

    private boolean isExistEmptyField() {
        boolean result = false;
        if (username.get().isEmpty()) {
            isUsernameEmpty.set(true);
            result = true;
        }
        if (password.get().isEmpty()) {
            isPasswordEmpty.set(true);
            result = true;
        }
        return result;
    }

    private class RegisterObserer extends DisposableObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            registerPublisher.onNext(result);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
