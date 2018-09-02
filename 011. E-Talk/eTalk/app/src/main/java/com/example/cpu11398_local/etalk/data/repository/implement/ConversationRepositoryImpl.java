package com.example.cpu11398_local.etalk.data.repository.implement;

import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import javax.inject.Inject;
import io.reactivex.Single;

public class ConversationRepositoryImpl  implements ConversationRepository{

    private NetworkSource networkSource;

    @Inject
    public ConversationRepositoryImpl(NetworkSource networkSource) {
        this.networkSource = networkSource;
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
}
