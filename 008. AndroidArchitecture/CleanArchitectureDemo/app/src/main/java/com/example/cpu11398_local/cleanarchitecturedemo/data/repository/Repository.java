package com.example.cpu11398_local.cleanarchitecturedemo.data.repository;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper.PagerMapper;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.Observable;

public class Repository {

    private LocalSource localSource;
    private PagerMapper pagerMapper;

    @Inject
    public Repository(LocalSource localSource, PagerMapper pagerMapper) {
        this.localSource = localSource;
        this.pagerMapper = pagerMapper;
    }

    public Observable<User> getData(String key) {
        return localSource.getData(key).map(pagerMapper::PagerModel2User);
    }

    public Observable<Boolean> putData(User user) {
        return localSource.putData(pagerMapper.User2PagerModel(user));
    }
}
