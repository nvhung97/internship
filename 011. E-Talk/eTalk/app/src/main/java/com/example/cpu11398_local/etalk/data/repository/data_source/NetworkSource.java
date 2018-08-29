package com.example.cpu11398_local.etalk.data.repository.data_source;

import android.graphics.Bitmap;

import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Optional;
import io.reactivex.Single;

public interface NetworkSource {

    /**
     * Load user's info from network base on given {@code username}.
     * @param username used as identifier to load info of user.
     * @return an observable contain a container {@code Optional} that contain
     * user's info if exist or contain {@code null}.
     */
    Single<Optional<User>> loadUser(String username);

    /**
     * Push new user to network database base on given {@code user}.
     * @param user data need to push to network database.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushUser(User user);

    /**
     * Check if user with given username exited.
     * @param username user need to check for existence.
     * @return an observable contain result of action. {@code true} if user existed,
     * otherwise {@code false}.
     */
    Single<Boolean> checkUserExisted(String username);

    /**
     * Schedule update user active time if user logging in. If the second parameter is true,
     * start update. Otherwise stop.
     * @param update {@code true} or {@code false}.
     */
    void updateUserActive(String username, Boolean update);

    /**
     * Upload image to network database.
     * @param image need to upload.
     * @param username user need to upload their image.
     * @return an observable contain link of image.
     */
    Single<String> uploadImage(String username, Bitmap image);
}
