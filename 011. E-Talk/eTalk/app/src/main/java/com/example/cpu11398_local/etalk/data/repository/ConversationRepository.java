package com.example.cpu11398_local.etalk.data.repository;

import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
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
    Observable<Message> loadNetworkMessages(String conversationKey);
}
