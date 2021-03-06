package com.example.cpu11398_local.etalk.data.repository.data_source;

import android.graphics.Bitmap;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Optional;

import java.io.File;

import io.reactivex.Observable;
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
     * Load user's info from network base on given {@code username} and update if it have
     * any changing.
     * @param username used as identifier to load info of user.
     * @return an observable contain a container {@code Optional} that contain
     * user's info if exist or contain {@code null}.
     */
    Observable<Optional<User>> loadChangeableUser(String username);

    /**
     * Load user's info from network base on given {@code phone}.
     * @param phone used to find user.
     * @return an observable contain a container {@code Optional} that contain
     * user's info if exist or contain {@code null}.
     */
    Single<Optional<User>> findUserWithPhone(String phone);

    /**
     * Push new user to network database base on given {@code user}.
     * @param user data need to push to network database.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushUser(User user);

    /**
     * Schedule update user active time if user logging in. If the second parameter is true,
     * start update. Otherwise stop.
     * @param update {@code true} or {@code false}.
     */
    void updateUserActive(String username, Boolean update);

    /**
     * Upload avatar of user to network database.
     * @param image need to upload.
     * @param username user need to upload avatar.
     * @return an observable contain link of image.
     */
    Single<String> uploadUserAvatar(String username, Bitmap image);

    /**
     * Push a new relationship to given {@code username} with a {@code conversation}.
     * @param username user id.
     * @param conversation used to get Key and type.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushRelationship(String username, Conversation conversation);

    /**
     * Push given {@code conversation} to network.
     * @param conversation
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushConversation(Conversation conversation);

    /**
     * Push given {@code message} into conversation with {@code conversationKey}.
     * @param conversationKey
     * @param message
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushMessage(String conversationKey, Message message);

    /**
     * Upload avatar of conversation to network database.
     * @param image need to upload.
     * @param conversationKey conversation need to upload avatar.
     * @return an observable contain link of image.
     */
    Single<String> uploadGroupAvatar(String conversationKey, Bitmap image);

    /**
     * Load all conversations of given {@code username}. Observe for changing.
     * @param username id of user.
     * @return an observable emit conversations of user.
     */
    Observable<Conversation> loadRelationships(String username);

    /**
     * Load a conversation and observe for changing by given {@code conversationKey}.
     * @param conversationKey
     * @return an observable emit result or any change about this conversation.
     */
    Observable<Conversation> loadConversation(String conversationKey);

    /**
     * Load all message of a conversation given by {@code conversationKey}.
     * @param conversationKey
     * @return an observable emit result or any new message.
     */
    Observable<Message> loadMessages(String conversationKey, String username);

    /**
     * Upload given file to network.
     * @param conversationKey conversation that file upload to.
     * @param file file need to upload.
     * @param code used to determine type of file.
     * @return an observable contain link of file.
     */
    Single<String> uploadFile(String conversationKey, File file, long code);

    /**
     * Download file from network and save to external storage.
     * @param url contain link and filename.
     * @return an observable contain event during download.
     */
    Observable<Event> downloadFile(String url);
}
