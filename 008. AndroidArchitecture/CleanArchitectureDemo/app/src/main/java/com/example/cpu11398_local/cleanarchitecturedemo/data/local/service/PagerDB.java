package com.example.cpu11398_local.cleanarchitecturedemo.data.local.service;

import android.content.Context;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.paperdb.Paper;
import io.reactivex.Observable;

public class PagerDB {

    @Inject
    public PagerDB(Context context) {
        Paper.init(context);
    }

    public Observable<List<PaperModel>> getData() {
        return Observable.create(
                emitter -> {
                    try {
                        List<PaperModel> listData = new ArrayList<>();
                        List<String> keys = Paper.book().getAllKeys();
                        for (String key : keys) {
                            listData.add(
                                    new PaperModel(
                                            key,
                                            String.valueOf(Paper.book().read(key))
                                    )
                            );
                        }
                        emitter.onNext(listData);
                        emitter.onComplete();
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
