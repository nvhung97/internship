package com.example.cpu11398_local.etalk.data.repository.implement;

import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import javax.inject.Inject;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository{

    private NetworkSource networkSource;

    @Inject
    public UserRepositoryImpl(NetworkSource networkSource) {
        this.networkSource = networkSource;
    }

    @Override
    public Single<Optional<User>> getNetworkUser(String username) {
        return networkSource.getUser(username);
    }

    @Override
    public void updateNetworkUserStatus(String username, String status) {
        networkSource.updateNetworkUserStatus(username, status);
    }
}
