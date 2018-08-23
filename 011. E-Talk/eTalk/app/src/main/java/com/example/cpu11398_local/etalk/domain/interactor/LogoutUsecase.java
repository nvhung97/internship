package com.example.cpu11398_local.etalk.domain.interactor;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class LogoutUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public LogoutUsecase(Executor executor,
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
        userRepository.updateNetworkUserActive("",false);
        userRepository.setCacheUser(null);
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
