package com.example.cpu11398_local.etalk.domain.interactor;

import android.util.Log;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class WelcomeUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public WelcomeUsecase(Executor executor,
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
        disposable.add(
                userRepository
                        .getCacheUsernameLoggedIn()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .map(this::onTask)
                        .subscribeWith((DisposableSingleObserver<Boolean>) observer)
        );
    }

    private boolean onTask(String username) {
        if (!username.isEmpty()) {
            userRepository.updateNetworkUserActive(username, true);
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
