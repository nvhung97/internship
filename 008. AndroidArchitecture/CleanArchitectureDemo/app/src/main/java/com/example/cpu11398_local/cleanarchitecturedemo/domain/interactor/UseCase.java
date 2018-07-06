package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import java.util.concurrent.Executor;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T, Params> {

    private Executor            subscribeThread;
    private Scheduler           observeScheduler;
    private CompositeDisposable disposable;

    /*public UseCase(Executor subscribeThread, Scheduler observeScheduler, CompositeDisposable disposable) {
        this.subscribeThread    = subscribeThread;
        this.observeScheduler   = observeScheduler;
        this.disposable         = disposable;
    }*/

    public UseCase() {
        this.disposable         = new CompositeDisposable();
    }

    abstract Observable<T> buildUseCaseObservable(Params params);

    /*public void execute(DisposableObserver<T> observer, Params params) {
        disposable.add(
                buildUseCaseObservable(params)
                        .subscribeOn(Schedulers.from(subscribeThread))
                        .observeOn(observeScheduler)
                        .subscribeWith(observer)

        );
    }*/

    public void execute(DisposableObserver<T> observer, Params params) {
        disposable.add(
                buildUseCaseObservable(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer)

        );
    }

    public void dispose() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
