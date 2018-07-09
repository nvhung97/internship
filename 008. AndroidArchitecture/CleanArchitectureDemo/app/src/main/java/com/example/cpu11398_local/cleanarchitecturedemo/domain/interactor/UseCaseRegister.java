package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

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
 * Created by Hung-pc on 7/6/2018.
 */

public class UseCaseRegister implements UseCase<Void, User> {

    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;

    @Inject
    public UseCaseRegister(Executor executor,
                           Scheduler scheduler,
                           CompositeDisposable disposable,
                           UserRepository userRepository) {
        this.executor       = executor;
        this.scheduler      = scheduler;
        this.disposable     = disposable;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(DisposableObserver<Void> observer, User params) {

    }

    @Override
    public void execute(DisposableSingleObserver<Void> observer, User params) {

    }

    @Override
    public void execute(DisposableCompletableObserver observer, User params) {
        disposable.add(
                userRepository
                        .putLocalUser(params)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(observer)
        );
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
