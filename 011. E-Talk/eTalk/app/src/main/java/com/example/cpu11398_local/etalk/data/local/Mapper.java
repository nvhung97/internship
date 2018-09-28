package com.example.cpu11398_local.etalk.data.local;

import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
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

    public static MessagesPersonHolder RoomMessagesPersonHolder2MessagesPersonHolder(ChatPersonUsecase usecase,
                                                                                     RoomMessagesPersonHolder roomMessagesPersonHolder) {
        return usecase.new MessagesPersonHolder(
                roomMessagesPersonHolder.getRawMessages(),
                roomMessagesPersonHolder.getSendingMessage(),
                roomMessagesPersonHolder.getMessages()
        );
    }

    public static RoomMessagesPersonHolder MessagesPersonHolder2RoomMessagesPersonHolder(String conversationKey,
                                                                                         MessagesPersonHolder holder) {
        return new RoomMessagesPersonHolder(
                conversationKey,
                holder
        );
    }

    public static MessagesGroupHolder RoomMessagesGroupHolder2MessagesGroupHolder(ChatGroupUsecase usecase,
                                                                                  RoomMessagesGroupHolder roomMessagesGroupHolder) {
        return usecase.new MessagesGroupHolder(
                roomMessagesGroupHolder.getRawMessages(),
                roomMessagesGroupHolder.getSendingMessage(),
                roomMessagesGroupHolder.getMessages()
        );
    }

    public static RoomMessagesGroupHolder MessagesGroupHolder2RoomMessagesGroupHolder(String conversationKey,
                                                                                      MessagesGroupHolder holder) {
        return new RoomMessagesGroupHolder(
                conversationKey,
                holder
        );
    }
}
