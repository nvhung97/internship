package com.example.cpu11398_local.etalk.domain.interactor;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RegisterUsecase implements Usecase {

    private final String DEFAULT_AVATAR = "https://firebasestorage.googleapis.com/v0/b/etalk-180808.appspot.com/o/avatars%2Fdefault.png?alt=media&token=b5e2840c-876e-4dcc-b1fd-d2f23f3d2d1b";

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public RegisterUsecase(Executor executor,
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
        String name     = (String)params[0];
        String username = (String)params[1];
        String password = (String)params[2];
        String phone    = (String)params[3];
        disposable.add(
                userRepository
                        .getNetworkUser(username)
                        .zipWith(
                                userRepository.findNetworkUserWithPhone(phone),
                                (user1, user2) -> user1.isPresent() || user2.isPresent()
                        )
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(isExisted -> {
                            if (isExisted) {
                                Single
                                        .just(false)
                                        .subscribeOn(Schedulers.from(executor))
                                        .observeOn(scheduler)
                                        .subscribe((DisposableSingleObserver<Boolean>)observer);
                            } else {
                                disposable.add(
                                        userRepository
                                                .setNetworkUser(new User(name, username, password, phone, DEFAULT_AVATAR))
                                                .subscribeOn(Schedulers.from(executor))
                                                .observeOn(scheduler)
                                                .subscribeWith((DisposableSingleObserver<Boolean>)observer)
                                );
                            }
                        })
        );
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
