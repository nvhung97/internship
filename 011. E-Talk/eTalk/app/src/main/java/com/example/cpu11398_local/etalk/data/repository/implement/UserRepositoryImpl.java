package com.example.cpu11398_local.etalk.data.repository.implement;

import android.graphics.Bitmap;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository{

    private NetworkSource   networkSource;
    private CacheSource     cacheSource;

    @Inject
    public UserRepositoryImpl(NetworkSource networkSource, CacheSource cacheSource) {
        this.networkSource  = networkSource;
        this.cacheSource    = cacheSource;
    }

    @Override
    public Single<Optional<User>> getNetworkUser(String username) {
        return networkSource.loadUser(username);
    }

    @Override
    public Single<Boolean> setNetworkUser(User user) {
        return networkSource.pushUser(user);
    }

    @Override
    public Single<Boolean> checkNetworkUserExisted(String username) {
        return networkSource.checkUserExisted(username);
    }

    @Override
    public void updateNetworkUserActive(String username, Boolean update) {
        networkSource.updateUserActive(username, update);
    }

    @Override
    public Single<String> uploadNetworkImage(String username, Bitmap image) {
        return networkSource.uploadImage(username, image);
    }

    @Override
    public void setCacheUser(User user) {
        cacheSource.cacheUser(user);
    }

    @Override
    public Single<User> getCacheUser() {
        return cacheSource.getUser();
    }

    @Override
    public Observable<User> getCacheChangeableUser() {
        return cacheSource.getChangeableUser();
    }

    @Override
    public Single<String> getCacheUsernameLoggedIn() {
        return cacheSource.getUsernameLoggedIn();
    }
}