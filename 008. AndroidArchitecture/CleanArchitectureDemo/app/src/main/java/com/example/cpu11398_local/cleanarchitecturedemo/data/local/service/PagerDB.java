package com.example.cpu11398_local.cleanarchitecturedemo.data.local.service;

import android.content.Context;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import javax.inject.Inject;
import io.paperdb.Paper;
import io.reactivex.Observable;

public class PagerDB {

    @Inject
    public PagerDB(Context context) {
        Paper.init(context);
    }

    public Observable<PaperModel> getData(String key) {
        return Observable.create(
                emitter -> {
                    try {
                        if (Paper.book().contains(key)) {
                            emitter.onNext(
                                    new PaperModel(
                                            key,
                                            String.valueOf(Paper.book().read(key))
                                    )
                            );
                            emitter.onComplete();
                        }
                        else {
                            emitter.onNext(null);
                            emitter.onComplete();
                        }
                    }
                    catch (Exception e) {
                        emitter.onError(e);
                    }
                }
        );
    }

    public Observable<Boolean> putData(PaperModel data) {
        return Observable.create(
                emitter -> {
                    try {
                        if (Paper.book().contains(data.getKey())) {
                            emitter.onNext(false);
                            emitter.onComplete();
                        } else {
                            Paper.book().write(
                                    data.getKey(),
                                    data.getValue()
                            );
                            emitter.onNext(true);
                            emitter.onComplete();
                        }
                    }
                    catch (Exception e) {
                        emitter.onError(e);
                    }
                }
        );
    }
}
