package com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import io.reactivex.Single;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public interface CacheSource {
    Single<User> getUser();
    void putUser(Optional<User> user);
}
