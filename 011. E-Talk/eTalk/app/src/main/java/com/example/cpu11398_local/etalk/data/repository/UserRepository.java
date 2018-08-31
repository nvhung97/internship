package com.example.cpu11398_local.etalk.data.repository;

import android.graphics.Bitmap;

import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import io.reactivex.Observable;
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
     * Load user's info from network base on given {@code phone}.
     * @param phone used to find user.
     * @return an observable contain a container {@code Optional} that contain
     * user's info if exist or contain {@code null}.
     */
    Single<Optional<User>> findNetworkFriendWithPhone(String phone);

    /**
     * Push new user to network database base on given {@code user}.
     * @param user data need to push to network database.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> setNetworkUser(User user);

    /**
     * Schedule update user active time if user logging in. If the second parameter is true,
     * start update. Otherwise stop.
     * @param update {@code true} or {@code false}.
     */
    void updateNetworkUserActive(String username, Boolean update);

    /**
     * Upload image to network database.
     * @param image need to upload.
     * @param username user need to upload their image.
     * @return an observable contain link of image.
     */
    Single<String> uploadNetworkImage(String username, Bitmap image);

    /**
     * Add friend given by {@code friend_username} to user given by {@code username}.
     * @param username id of user need to add friend.
     * @param friend_username id of friend.
     * @return an observable contain result of request. {@code true} if success and
     * {@code false} if fail.
     */
    Single<Boolean> addNetworkFriend(String username, String friend_username);

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
     * Get user's info cached and might changed in the future.
     * @return an observable contain user.
     */
    Observable<User> getCacheChangeableUser();

    /**
     * Get username of user logged in. if have, go straight to content view.
     * @return an observable contain username or empty string.
     */
    Single<String> getCacheUsernameLoggedIn();
}
