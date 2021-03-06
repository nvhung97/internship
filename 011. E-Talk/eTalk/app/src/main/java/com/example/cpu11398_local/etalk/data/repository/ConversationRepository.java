package com.example.cpu11398_local.etalk.data.repository;

import android.graphics.Bitmap;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.utils.Event;

import java.io.File;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ConversationRepository {

    /**
     * Push a new relationship to given {@code username} with a {@code conversation}.
     * @param username user id.
     * @param conversation used to get key and type.
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushNetworkRelationship(String username, Conversation conversation);

    /**
     * Push given {@code conversation} to network.
     * @param conversation
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushNetworkConversation(Conversation conversation);

    /**
     * Push given {@code message} into conversation with {@code conversationKey}.
     * @param conversationKey
     * @param message
     * @return an observable contain result of action. {@code true} if successfully,
     * otherwise {@code false}.
     */
    Single<Boolean> pushNetworkMessage(String conversationKey, Message message);

    /**
     * Upload avatar of conversation to network database.
     * @param image need to upload.
     * @param conversationKey conversation need to upload avatar.
     * @return an observable contain link of image.
     */
    Single<String> uploadNetworkGroupAvatar(String conversationKey, Bitmap image);

    /**
     * Load all conversations of given {@code username}. Observe for changing.
     * @param username id of user.
     * @return an observable emit conversations of user.
     */
    Observable<Conversation> loadNetworkRelationships(String username);

    /**
     * Load a conversation and observe for changing by given {@code conversationKey}.
     * @param conversationKey
     * @return an observable emit result or any change about this conversation.
     */
    Observable<Conversation> loadNetworkConversation(String conversationKey);

    /**
     * Load all message of a conversation given by {@code conversationKey}.
     * @param conversationKey
     * @return an observable emit result or any new message.
     */
    Observable<Message> loadNetworkMessages(String conversationKey, String username);

    /**
     * Upload given file to network.
     * @param conversationKey conversation that file upload to.
     * @param file file need to upload.
     * @param code used to determine type of file.
     * @return an observable contain link of file.
     */
    Single<String> uploadNetworkFile(String conversationKey, File file, long code);

    /**
     * Download file from network and save to external storage.
     * @param url contain link and filename.
     * @return an observable contain event during download.
     */
    Observable<Event> downloadNetworkFile(String url);

    /**
     * Load container that contain messages of conversation given by {@code conversationKey}.
     * @param usecase
     * @param conversationKey
     * @return
     */
    Single<MessagesPersonHolder> loadLocalMessagesPersonHolder(ChatPersonUsecase usecase, String conversationKey);

    /**
     * Save container contain messages to local database.
     * @param conversationKey
     * @param chatHolder
     */
    void putLocalMessagesPersonHolder(String conversationKey, MessagesPersonHolder chatHolder);

    void deleteAllLocalMessagesPersonHolder();

    /**
     * Load container that contain messages of conversation given by {@code conversationKey}.
     * @param usecase
     * @param conversationKey
     * @return
     */
    Single<MessagesGroupHolder> loadLocalMessagesGroupHolder(ChatGroupUsecase usecase, String conversationKey);

    /**
     * Save container contain messages to local database.
     * @param conversationKey
     * @param messagesGroupHolder
     */
    void putLocalMessagesGroupHolder(String conversationKey, MessagesGroupHolder messagesGroupHolder);

    void deleteAllLocalMessagesGroupHolder();

    Single<List<Conversation>> loadAllLocalConversation();

    Single<Conversation> loadLocalConversation(String conversatonKey);

    void insertLocalConversation(Conversation conversation);

    void deleteAllLocalConversation();
}
