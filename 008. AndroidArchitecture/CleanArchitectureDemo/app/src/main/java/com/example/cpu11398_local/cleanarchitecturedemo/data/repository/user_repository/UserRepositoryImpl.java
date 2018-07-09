package com.example.cpu11398_local.cleanarchitecturedemo.data.repository.user_repository;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public class UserRepositoryImpl implements UserRepository {

    private LocalSource localSource;
    private CacheSource cacheSource;

    @Inject
    public UserRepositoryImpl(LocalSource localSource, CacheSource cacheSource) {
        this.localSource = localSource;
        this.cacheSource = cacheSource;
    }

    @Override
    public Single<Optional<User>> getLocalUser(String username) {
        return localSource.getUser(username).doOnSuccess(cacheSource::putUser);
    }

    @Override
    public Completable putLocalUser(User user) {
        return localSource.putUser(user);
    }

    @Override
    public Single<User> getCacheUser() {
        return cacheSource.getUser();
    }
}
