package com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.service.PaperDB;
import javax.inject.Inject;
import io.reactivex.Observable;

public class LocalSource {

    private PaperDB paperDB;

    @Inject
    public LocalSource(PaperDB paperDB) {
        this.paperDB = paperDB;
    }

    public Observable<PaperModel> getData(String key) {
        return paperDB.getData(key);
    }

    public Observable<Boolean> putData(PaperModel data) {
        return paperDB.putData(data);
    }
}
