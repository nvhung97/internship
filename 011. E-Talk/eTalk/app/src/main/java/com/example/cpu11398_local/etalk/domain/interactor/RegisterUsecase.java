package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.util.Log;
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
                                userRepository.findNetworkFriendWithPhone(phone),
                                (user1, user2) -> user1.isPresent() || user2.isPresent()
                        )
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<Boolean>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void onSuccess(Boolean isExisted) {
                                if (isExisted) {
                                    Single
                                            .just(false)
                                            .subscribeOn(Schedulers.from(executor))
                                            .observeOn(scheduler)
                                            .subscribeWith((DisposableSingleObserver<Boolean>)observer);
                                } else {
                                    userRepository
                                            .setNetworkUser(
                                                    new User(
                                                            name,
                                                            username,
                                                            password,
                                                            phone
                                                    )
                                            )
                                            .subscribeOn(Schedulers.from(executor))
                                            .observeOn(scheduler)
                                            .subscribeWith((DisposableSingleObserver<Boolean>)observer);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("eTalk", e.getMessage());
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
