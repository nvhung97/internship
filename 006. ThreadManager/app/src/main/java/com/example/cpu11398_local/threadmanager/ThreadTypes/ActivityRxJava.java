package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.cpu11398_local.threadmanager.R;
import com.example.cpu11398_local.threadmanager.Utils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivityRxJava extends AppCompatActivity {

    private final  String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        Observable
                .just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {

                    @Override public void onComplete() {
                        Utils.showLog(TAG, Thread.currentThread().getId(), "onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        Utils.showLog(TAG, Thread.currentThread().getId(), "onError(): " + e.getMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        Utils.showLog(TAG, Thread.currentThread().getId(), "onSubscribe(): " + d.toString());
                    }

                    @Override public void onNext(Integer integer) {
                        Utils.showLog(TAG, Thread.currentThread().getId(), "onNext(" + integer +")");
                    }
                });
    }
}
