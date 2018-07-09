package com.example.cpu11398_local.cleanarchitecturedemo.data.repository.user_repository;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public interface UserRepository {
    Single<Optional<User>> getLocalUser(String id);
    Completable putLocalUser(User user);
    Single<User> getCacheUser();
}
