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

public class UseCaseLogin implements UseCase<Boolean, User> {

    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;

    @Inject
    public UseCaseLogin(Executor executor,
                        Scheduler scheduler,
                        CompositeDisposable disposable,
                        UserRepository userRepository) {
        this.executor       = executor;
        this.scheduler      = scheduler;
        this.disposable     = disposable;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(DisposableObserver<Boolean> observer, User params) {

    }

    @Override
    public void execute(DisposableSingleObserver<Boolean> observer, User params) {
        disposable.add(
                userRepository
                        .getLocalUser(params.getUsername())
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .map(item -> onTask(item, params))
                        .subscribeWith(observer)
        );
    }

    @Override
    public void execute(DisposableCompletableObserver observer, User params) {

    }

    private Boolean onTask(Optional<User> user, User newUser) {
        if (user.isPresent()
                && newUser.getUsername().equals(user.get().getUsername())
                && newUser.getPassword().equals(user.get().getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
