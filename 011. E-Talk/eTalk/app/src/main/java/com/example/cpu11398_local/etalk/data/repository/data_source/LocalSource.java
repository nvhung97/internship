package com.example.cpu11398_local.etalk.data.repository.data_source;

import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.List;
import io.reactivex.Single;

public interface LocalSource {

    Single<List<User>> loadAllUser();

    Single<User> loadUser(String username);

    void inserUser(User user);

    void deleteAllUser();

    Single<List<Conversation>> loadAllConversation();

    Single<Conversation> loadConversation(String conversatonKey);

    void insertConversation(Conversation conversation);

    void deleteAllConversation();

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
     * @param messagesPersonHolder
     */
    void putLocalMessagesPersonHolder(String conversationKey, MessagesPersonHolder messagesPersonHolder);

    /**
     * Delete all entry in MessagesPersonHolder
     */
    void deleteAllMessagesPersonHolder();

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

    /**
     * Delete all entry in MessagesGroupHolder
     */
    void deleteAllMessagesGroupHolder();
}
