package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.Repository;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class UseCaseLogin extends UseCase<User, Boolean, User>{

    @Inject
    public UseCaseLogin(Executor subscribeThread,
                           Scheduler observeScheduler,
                           CompositeDisposable disposable,
                           Repository repository) {
        super(
                subscribeThread,
                observeScheduler,
                disposable,
                repository
        );
    }

    @Override
    Observable<User> buildUseCaseObservable(User user) {
        return getRepository().getLocal(user.getUsername());
    }

    @Override
    void doOnResponseSuccess(User user) {
        if (getParams().getUsername().equals(user.getUsername())
                && getParams().getPassword().equals(user.getPassword())) {
            publishResult(true);
        }
        else {
            publishResult(false);
        }
    }

    @Override
    void doOnResponseFail(Throwable e) {
        publishResult(false);
    }
}
