package com.example.cpu11398_local.etalk.data.repository.implement;

import android.graphics.Bitmap;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;

import java.util.List;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ConversationRepositoryImpl  implements ConversationRepository{

    private NetworkSource networkSource;
    private LocalSource   localSource;

    @Inject
    public ConversationRepositoryImpl(NetworkSource networkSource,
                                      LocalSource localSource) {
        this.networkSource = networkSource;
        this.localSource   = localSource;
    }

    @Override
    public Single<Boolean> pushNetworkRelationship(String username, Conversation conversation) {
        return networkSource.pushRelationship(username, conversation);
    }

    @Override
    public Single<Boolean> pushNetworkConversation(Conversation conversation) {
        return networkSource.pushConversation(conversation);
    }

    @Override
    public Single<Boolean> pushNetworkMessage(String conversationKey, Message message) {
        return networkSource.pushMessage(conversationKey, message);
    }

    @Override
    public Single<String> uploadNetworkGroupAvatar(String conversationKey, Bitmap image) {
        return networkSource.uploadGroupAvatar(conversationKey, image);
    }

    @Override
    public Observable<Conversation> loadNetworkRelationships(String username) {
        return networkSource.loadRelationships(username);
    }

    @Override
    public Observable<Conversation> loadNetworkConversation(String conversationKey) {
        return networkSource.loadConversation(conversationKey);
    }

    @Override
    public Observable<Message> loadNetworkMessages(String conversationKey, String username) {
        return networkSource.loadMessages(conversationKey, username);
    }

    @Override
    public Single<MessagesPersonHolder> loadLocalMessagesPersonHolder(ChatPersonUsecase usecase, String conversationKey) {
        return localSource.loadLocalMessagesPersonHolder(usecase, conversationKey);
    }

    @Override
    public void putLocalMessagesPersonHolder(String conversationKey, MessagesPersonHolder chatHolder) {
        localSource.putLocalMessagesPersonHolder(conversationKey, chatHolder);
    }

    @Override
    public void deleteAllLocalMessagesPersonHolder() {
        localSource.deleteAllMessagesPersonHolder();
    }

    @Override
    public Single<MessagesGroupHolder> loadLocalMessagesGroupHolder(ChatGroupUsecase usecase, String conversationKey) {
        return localSource.loadLocalMessagesGroupHolder(usecase, conversationKey);
    }

    @Override
    public void putLocalMessagesGroupHolder(String conversationKey, MessagesGroupHolder chatHolder) {
        localSource.putLocalMessagesGroupHolder(conversationKey, chatHolder);
    }

    @Override
    public void deleteAllLocalMessagesGroupHolder() {
        localSource.deleteAllMessagesGroupHolder();
    }

    @Override
    public Single<List<Conversation>> loadAllLocalConversation() {
        return localSource.loadAllConversation();
    }

    @Override
    public Single<Conversation> loadLocalConversation(String conversatonKey) {
        return localSource.loadConversation(conversatonKey);
    }

    @Override
    public void insertLocalConversation(Conversation conversation) {
        localSource.insertConversation(conversation);
    }

    @Override
    public void deleteAllLocalConversation() {
        localSource.deleteAllConversation();
    }
}
