package com.example.cpu11398_local.etalk.domain.interactor;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hung-pc on 8/30/2018.
 */

public class FindFriendUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public FindFriendUsecase(Executor executor,
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
        String data = (String)params[0];
        String type = (String)params[1];
        switch (type) {
            case "phone":
                disposable.add(
                        userRepository
                                .findNetworkUserWithPhone(data)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribeWith((DisposableSingleObserver<Optional<User>>)observer)
                );
                break;
            case "name":
                break;
            case "username":
                break;
        }
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
