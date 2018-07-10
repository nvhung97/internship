package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCase;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subjects.PublishSubject;

public class RegisterViewModel implements ViewModel<Boolean>{

    public ObservableField<String>  username          = new ObservableField<>("");
    public ObservableField<String>  password          = new ObservableField<>("");
    public ObservableField<Boolean> isUsernameEmpty   = new ObservableField<>(false);
    public ObservableField<Boolean> isPasswordEmpty   = new ObservableField<>(false);
    public PublishSubject<Boolean>  registerPublisher = PublishSubject.create();

    private UseCase  useCaseRegister;

    @Inject
    public RegisterViewModel(@Named("UseCaseRegister") UseCase useCaseRegister) {
        this.useCaseRegister = useCaseRegister;
    }

    @Override
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

    private class RegisterObserer extends DisposableCompletableObserver {
        @Override
        public void onComplete() {
            registerPublisher.onNext(true);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("CleanArchitecture", e.getMessage());
            registerPublisher.onNext(false);
        }
    }

    @Override
    public void endTask() {
        useCaseRegister.endTask();
    }
}
