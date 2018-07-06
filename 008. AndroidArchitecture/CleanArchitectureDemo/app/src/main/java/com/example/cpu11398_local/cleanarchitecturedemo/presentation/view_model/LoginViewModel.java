package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import android.view.View;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel {

    public ObservableField<String>  username        = new ObservableField<>("");
    public ObservableField<String>  password        = new ObservableField<>("");
    public ObservableField<Boolean> isUsernameEmpty = new ObservableField<>(false);
    public ObservableField<Boolean> isPasswordEmpty = new ObservableField<>(false);

    private PublishSubject<Boolean>  loginPublisher  = PublishSubject.create();

    public void subscribeObserver(Observer<Boolean> observer) {
        loginPublisher.subscribe(observer);
    }

    public void performLogin(View v) {
        if (!isExistEmptyField()){
            loginPublisher.onNext(true);
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
}