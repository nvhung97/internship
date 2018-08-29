package com.example.cpu11398_local.etalk.domain.interactor;

import android.graphics.Bitmap;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateUserInfoUsecase implements Usecase {

    private Executor            executor;
    private Scheduler           scheduler;
    private CompositeDisposable disposable;
    private UserRepository      userRepository;

    @Inject
    public UpdateUserInfoUsecase(Executor executor,
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
        User    user    = (User)params[0];
        Bitmap  bitmap  = (Bitmap)params[1];
        if (bitmap == null) {
            disposable.add(
                    userRepository
                            .setNetworkUser(user)
                            .doOnSuccess(aBoolean -> userRepository.setCacheUser(user))
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribeWith((DisposableSingleObserver<Boolean>)observer)
            );
        } else {
            disposable.add(
                    userRepository
                            .uploadNetworkImage(user.getUsername(), bitmap)
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribeWith(new DisposableSingleObserver<String>() {
                                @Override
                                public void onSuccess(String url) {
                                    user.setAvatar(url);
                                    disposable.add(
                                            userRepository
                                                    .setNetworkUser(user)
                                                    .doOnSuccess(aBoolean -> userRepository.setCacheUser(user))
                                                    .subscribeOn(Schedulers.from(executor))
                                                    .observeOn(scheduler)
                                                    .subscribeWith((DisposableSingleObserver<Boolean>)observer)
                                    );
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i("eTalk", e.getMessage());
                                }
                            })
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