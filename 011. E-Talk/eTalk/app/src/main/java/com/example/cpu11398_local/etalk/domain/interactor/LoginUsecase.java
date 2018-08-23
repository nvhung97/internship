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

public class LoginUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public LoginUsecase(Executor executor,
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
        String username = (String)params[0];
        String password = (String)params[1];
        disposable.add(
                userRepository
                        .getNetworkUser(username)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .map(item -> onTask(item, username, password))
                        .subscribeWith((DisposableSingleObserver<Boolean>)observer)
        );
    }

    private Boolean onTask(Optional<User> user, String username, String password) {
        if (user.isPresent()
                && username.equals(user.get().getUsername())
                && password.equals(user.get().getPassword())) {
            userRepository.setCacheUser(user.get());
            userRepository.updateNetworkUserActive(
                    user.get().getUsername(),
                    true
            );
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
