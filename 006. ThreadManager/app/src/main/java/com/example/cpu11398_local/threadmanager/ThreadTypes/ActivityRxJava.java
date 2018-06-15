package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.cpu11398_local.threadmanager.R;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ActivityRxJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        Observable
                .just(1, 2, 3)
                .subscribe(new Observer<Integer>() {

                    @Override public void onComplete() {
                        Log.d("Test", "In onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        Log.d("Test", "In onError()");
                    }


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override public void onNext(Integer integer) {
                        Log.d("Test", "In onNext():" + integer);
                    }
                });
    }
}
