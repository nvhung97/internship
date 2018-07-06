package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.Repository;
import java.util.concurrent.Executor;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T, O, P> {

    private Executor                subscribeThread;
    private Scheduler               observeScheduler;
    private CompositeDisposable     disposable;
    private Repository              repository;
    private DisposableObserver<O>   observer;
    private P                       params;

    public UseCase(Executor subscribeThread,
                   Scheduler observeScheduler,
                   CompositeDisposable disposable,
                   Repository repository) {
        this.subscribeThread    = subscribeThread;
        this.observeScheduler   = observeScheduler;
        this.disposable         = disposable;
        this.repository         = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public P getParams() {
        return params;
    }

    abstract Observable<T> buildUseCaseObservable(P params);

    public void execute(DisposableObserver<O> observer, P params) {
        this.observer = observer;
        this.params   = params;
        disposable.add(
                buildUseCaseObservable(params)
                        .subscribeOn(Schedulers.from(subscribeThread))
                        .observeOn(observeScheduler)
                        .subscribeWith(new UseCaseObserver())

        );
    }

    abstract void doOnResponseSuccess(T t);
    abstract void doOnResponseFail(Throwable e);

    private class UseCaseObserver extends DisposableObserver<T> {
        @Override
        public void onNext(T t) {
            doOnResponseSuccess(t);
        }

        @Override
        public void onError(Throwable e) {
            doOnResponseFail(e);
        }

        @Override
        public void onComplete() {

        }
    }

    public void publishResult(O result) {
        Observable<O> observable = Observable.create(
                emitter -> {
                    emitter.onNext(result);
                    emitter.onComplete();
                }
        );
        observable.subscribe(observer);
    }

    public void publishError(Throwable e) {
        Observable<O> observable = Observable.create(
                emitter -> emitter.onError(e)
        );
        observable.subscribe(observer);
    }

    public void dispose() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
