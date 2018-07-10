package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCase;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel implements ViewModel<Boolean>{

    public ObservableField<String>  username        = new ObservableField<>("");
    public ObservableField<String>  password        = new ObservableField<>("");
    public ObservableField<Boolean> isUsernameEmpty = new ObservableField<>(false);
    public ObservableField<Boolean> isPasswordEmpty = new ObservableField<>(false);
    public PublishSubject<Boolean>  loginPublisher  = PublishSubject.create();

    private UseCase useCaseLogin;

    @Inject
    public LoginViewModel(@Named("UseCaseLogin") UseCase useCaseLogin) {
        this.useCaseLogin = useCaseLogin;
    }

    @Override
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

    private class LoginObserer extends DisposableSingleObserver<Boolean> {
        @Override
        public void onSuccess(Boolean isSuccess) {
            if (isSuccess) {
                loginPublisher.onNext(true);
            } else {
                loginPublisher.onNext(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e("CleanArchitecture", e.getMessage());
        }
    }
    @Override
    public void endTask() {
        useCaseLogin.endTask();
    }
}