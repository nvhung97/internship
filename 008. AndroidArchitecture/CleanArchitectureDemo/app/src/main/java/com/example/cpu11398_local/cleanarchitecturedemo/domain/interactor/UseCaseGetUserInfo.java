package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.user_repository.UserRepository;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public class UseCaseGetUserInfo implements UseCase<User, Void> {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public UseCaseGetUserInfo(Executor executor,
                              Scheduler scheduler,
                              CompositeDisposable disposable,
                              UserRepository userRepository) {
        this.executor       = executor;
        this.scheduler      = scheduler;
        this.disposable     = disposable;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(DisposableObserver<User> observer, Void params) {

    }

    @Override
    public void execute(DisposableSingleObserver<User> observer, Void params) {
        disposable.add(
                userRepository
                        .getCacheUser()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(observer)
        );
    }

    @Override
    public void execute(DisposableCompletableObserver observer, Void params) {

    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
