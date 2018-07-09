package com.example.cpu11398_local.cleanarchitecturedemo.data.local.realm;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public class RealmDB implements LocalSource{

    @Override
    public Single<Optional<User>> getUser(String username) {
        //TODO
        return null;
    }

    @Override
    public Completable putUser(User user) {
        //TODO
        return null;
    }
}
