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
     * Check if user logged in. if true, go straight to content view.
     * @return an observable contain {@code true} or {@code false}.
     */
    Single<Boolean> checkUserLoggedIn();
}
