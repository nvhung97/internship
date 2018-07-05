package com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.service.PagerDB;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Observable;

public class LocalSource {

    private PagerDB pagerDB;

    @Inject
    public LocalSource(PagerDB pagerDB) {
        this.pagerDB = pagerDB;
    }

    public Observable<List<PaperModel>> getData() {
        return pagerDB.getData();
    }

    public Observable<Boolean> putData(PaperModel data) {
        return pagerDB.putData(data);
    }
}
