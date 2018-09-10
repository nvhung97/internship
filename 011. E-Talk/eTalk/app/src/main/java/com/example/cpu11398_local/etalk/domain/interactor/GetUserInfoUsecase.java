package com.example.cpu11398_local.etalk.domain.interactor;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GetUserInfoUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public GetUserInfoUsecase(Executor executor,
                              Scheduler scheduler,
                              CompositeDisposable disposable,
                              UserRepository userRepository) {
        this.executor       = executor;
        this.scheduler      = scheduler;
        this.disposable     = disposable;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(Object observer, Object... params) {
        boolean getChangeableUser = (boolean)params[0];
        if (getChangeableUser) {
            /*disposable.add(
                    userRepository
                            .getCacheChangeableUser()
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribeWith((DisposableObserver<User>)observer)
            );*/
        } else {
            disposable.add(
                    userRepository
                            .getCacheUser()
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribeWith((DisposableSingleObserver<User>)observer)
            );
        }
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}