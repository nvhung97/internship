package com.example.cpu11398_local.cleanarchitecturedemo.data.repository;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper.PaperMapper;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import io.reactivex.Observable;

public class Repository {

    private LocalSource localSource;
    private PaperMapper pagerMapper;

    @Inject
    public Repository(LocalSource localSource, PaperMapper pagerMapper) {
        this.localSource = localSource;
        this.pagerMapper = pagerMapper;
    }

    public Observable<User> getLocal(String key) {
        return localSource.getData(key).map(pagerMapper::PagerModel2User);
    }

    public Observable<Boolean> putLocal(User user) {
        return localSource.putData(pagerMapper.User2PagerModel(user));
    }
}
