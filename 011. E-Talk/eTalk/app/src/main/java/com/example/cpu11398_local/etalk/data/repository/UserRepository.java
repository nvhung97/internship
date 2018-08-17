package com.example.cpu11398_local.etalk.data.repository;

import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import io.reactivex.Single;

public interface UserRepository {

    /**
     * Load user's info from network base on given {@code username}.
     * @param username used as identifier to load info of user.
     * @return an observable contain a container {@code Optional} that contain
     * user's info if exist or contain {@code null}.
     */
    Single<Optional<User>> getNetworkUser(String username);

    /**
     * Push new user to network database base on given {@code user}.
     * @param user data need to push to network database.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> putNetworkUser(User user);

    /**
     * Set status of given user by {@code username} to {@code ONLINE} when user login or
     * {@code OFFLINE} when user logout.
     * @param status {@code ONLINE} or {@code OFFLINE}.
     */
    void updateNetworkUserStatus(String username, String status);
}
