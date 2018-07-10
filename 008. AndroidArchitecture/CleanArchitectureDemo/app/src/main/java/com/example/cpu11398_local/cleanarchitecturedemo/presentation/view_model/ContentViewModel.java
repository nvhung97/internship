package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model;

import android.databinding.ObservableField;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCase;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public class ContentViewModel implements ViewModel<Void>{

    public  ObservableField<String> username = new ObservableField<>("");

    private UseCase useCaseGetUserInfo;

    @Inject
    public ContentViewModel(@Named("UseCaseGetUserInfo") UseCase useCaseGetUserInfo) {
        this.useCaseGetUserInfo = useCaseGetUserInfo;
        useCaseGetUserInfo.execute(
                new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        username.set(user.getUsername());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                },
                null
        );
    }

    @Override
    public void subscribeObserver(Observer<Void> observer) {

    }

    @Override
    public void endTask() {
        useCaseGetUserInfo.endTask();
    }
}
