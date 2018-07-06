package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import android.view.View;

import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseLogin;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel {

    public ObservableField<String>  username        = new ObservableField<>("");
    public ObservableField<String>  password        = new ObservableField<>("");
    public ObservableField<Boolean> isUsernameEmpty = new ObservableField<>(false);
    public ObservableField<Boolean> isPasswordEmpty = new ObservableField<>(false);
    public PublishSubject<Boolean>  loginPublisher  = PublishSubject.create();

    private UseCaseLogin            useCaseLogin;

    @Inject
    public LoginViewModel(UseCaseLogin useCaseLogin) {
        this.useCaseLogin = useCaseLogin;
    }

    public void subscribeObserver(Observer<Boolean> observer) {
        loginPublisher.subscribe(observer);
    }

    public void performLogin(View v) {
        if (!isExistEmptyField()){
            useCaseLogin.execute(
                    new LoginObserer(),
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

    private class LoginObserer extends DisposableObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            if (result){
                username.set("");
                password.set("");
                isUsernameEmpty.set(false);
                isPasswordEmpty.set(false);
            }
            loginPublisher.onNext(result);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}