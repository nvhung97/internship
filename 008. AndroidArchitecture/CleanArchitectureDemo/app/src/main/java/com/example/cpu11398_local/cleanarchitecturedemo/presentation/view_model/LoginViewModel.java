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
    public PublishSubject<Boolean>  publishSubject  = PublishSubject.create();

    public void subscribeObserver(Observer<Boolean> observer) {
        publishSubject.subscribe(observer);
    }

    public void performLogin(View v) {
        if (!isExistEmptyField()){
            publishSubject.onNext(true);
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
