package com.example.cpu11398_local.etalk.data.local;

import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesHolder;

public class Mapper {

    public static MessagesHolder ChatHolder2MessagesHolder(ChatPersonUsecase usecase,
                                                           ChatHolder chatHolder) {
        return usecase.new MessagesHolder(
                chatHolder.getRawMessages(),
                chatHolder.getSendingMessage(),
                chatHolder.getMessages()
        );
    }

    public static ChatHolder MessagesHolder2ChatHolder(String conversationKey,
                                                       MessagesHolder holder) {
        return new ChatHolder(
                conversationKey,
                holder
        );
    }
}
