package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.Repository;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Hung-pc on 7/6/2018.
 */

public class UseCaseRegister extends UseCase<Boolean, Boolean, User> {

    @Inject
    public UseCaseRegister(Executor subscribeThread,
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
    Observable<Boolean> buildUseCaseObservable(User user) {
        return getRepository().putLocal(user);
    }

    @Override
    void doOnResponseSuccess(Boolean result) {
        publishResult(result);
    }

    @Override
    void doOnResponseFail(Throwable e) {
        publishError(e);
    }
}
