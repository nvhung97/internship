package com.example.cpu11398_local.etalk.data.repository.data_source;
import com.example.cpu11398_local.etalk.presentation.model.User;

import io.reactivex.Single;

public interface CacheSource {

    /**
     * Cache user's info when user login to retrieve in the future.
     * @param user need to cache.
     */
    void cacheUser(User user);

    /**
     * Get user's info cached. Called after execute method {@link #cacheUser(User)}.
     * @return an observable contain user.
     */
    Single<User> getUser();

    /**
     * Get username of user logged in. if have, go straight to content view.
     * @return an observable contain username or empty string.
     */
    Single<String> getUsernameLoggedIn();
}
