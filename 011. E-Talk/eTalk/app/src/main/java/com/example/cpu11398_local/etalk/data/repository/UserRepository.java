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
    Single<Boolean> setNetworkUser(User user);

    /**
     * Check if user with given username exited.
     * @param username user need to check for existence.
     * @return an observable contain result of action. {@code true} if user existed,
     * otherwise {@code false}.
     */
    Single<Boolean> checkNetworkUserExisted(String username);

    /**
     * Set status of given user by {@code username} to {@code ONLINE} when user login or
     * {@code OFFLINE} when user logout.
     * @param status {@code ONLINE} or {@code OFFLINE}.
     */
    void updateNetworkUserStatus(String username, String status);

    /**
     * Cache user's info when user login to retrieve in the future.
     * @param user need to cache.
     */
    void setCacheUser(User user);

    /**
     * Get user's info cached.
     * @return an observable contain user.
     */
    Single<User> getCacheUser();

    /**
     * Check if user logged in. if true, go straight to content view.
     * @return an observable contain {@code true} or {@code false}.
     */
    Single<Boolean> checkCacheUserLoggedIn();
}
