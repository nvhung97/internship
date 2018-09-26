package com.example.cpu11398_local.etalk.data.repository.data_source;

import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesHolder;
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
    Single<MessagesHolder> loadLocalMessagesHolder(ChatPersonUsecase usecase, String conversationKey);

    /**
     * Save container contain messages to local database.
     * @param conversationKey
     * @param chatHolder
     */
    void putLocalMessagesHolder(String conversationKey, MessagesHolder chatHolder);

    void deleteAllMessagesHolder();
}
