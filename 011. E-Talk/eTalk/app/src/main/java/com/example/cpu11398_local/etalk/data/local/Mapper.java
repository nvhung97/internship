package com.example.cpu11398_local.etalk.data.local;

import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesHolder;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;

public class Mapper {

    public static User RoomUser2User(RoomUser roomUser) {
        return new User(
                roomUser.getName(),
                roomUser.getUsername(),
                roomUser.getPassword(),
                roomUser.getPhone(),
                roomUser.getAvatar(),
                roomUser.getActive()
        );
    }

    public static RoomUser User2RoomUser(User user) {
        return new RoomUser(
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getPhone(),
                user.getAvatar(),
                user.getActive()
        );
    }

    public static Conversation RoomConversation2Conversation(RoomConversation roomConversation) {
        return new Conversation(
                roomConversation.getKey(),
                roomConversation.getType(),
                roomConversation.getName(),
                roomConversation.getAvatar(),
                roomConversation.getCreator(),
                roomConversation.getCreateTime(),
                roomConversation.getMembers(),
                roomConversation.getLastMessage()
        );
    }

    public static RoomConversation Conversation2RoomConversation(Conversation conversation) {
        return new RoomConversation(
                conversation.getKey(),
                conversation.getType(),
                conversation.getName(),
                conversation.getAvatar(),
                conversation.getCreator(),
                conversation.getTime(),
                conversation.getMembers(),
                conversation.getLastMessage()
        );
    }

    public static MessagesHolder RoomMessagesHolder2MessagesHolder(ChatPersonUsecase usecase,
                                                                   RoomMessagesHolder roomMessagesHolder) {
        return usecase.new MessagesHolder(
                roomMessagesHolder.getRawMessages(),
                roomMessagesHolder.getSendingMessage(),
                roomMessagesHolder.getMessages()
        );
    }

    public static RoomMessagesHolder MessagesHolder2RoomMessagesHolder(String conversationKey,
                                                                       MessagesHolder holder) {
        return new RoomMessagesHolder(
                conversationKey,
                holder
        );
    }
}
