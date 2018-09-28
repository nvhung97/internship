package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
import java.util.List;
import java.util.Map;

@Entity
public class RoomMessagesPersonHolder {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String conversationKey;

    @NonNull
    @ColumnInfo(name = "raw_message")
    Map<String, Message> rawMessages;

    @NonNull
    @ColumnInfo(name = "sending_message")
    Map<String, Message> sendingMessage;

    @NonNull
    @ColumnInfo(name = "item")
    List<MessagePersonItem> messages;

    public RoomMessagesPersonHolder(@NonNull String conversationKey,
                                    @NonNull MessagesPersonHolder holder) {
        this.conversationKey = conversationKey;
        this.rawMessages     = holder.getRawMessages();
        this.sendingMessage  = holder.getSendingMessage();
        this.messages        = holder.getMessages();
    }

    public RoomMessagesPersonHolder(@NonNull String conversationKey,
                                    @NonNull Map<String, Message> rawMessages,
                                    @NonNull Map<String, Message> sendingMessage,
                                    @NonNull List<MessagePersonItem> messages) {
        this.conversationKey = conversationKey;
        this.rawMessages     = rawMessages;
        this.sendingMessage  = sendingMessage;
        this.messages        = messages;
    }

    @NonNull
    public String getConversationKey() {
        return conversationKey;
    }

    public void setConversationKey(@NonNull String conversationKey) {
        this.conversationKey = conversationKey;
    }

    @NonNull
    public Map<String, Message> getRawMessages() {
        return rawMessages;
    }

    public void setRawMessages(@NonNull Map<String, Message> rawMessages) {
        this.rawMessages = rawMessages;
    }

    @NonNull
    public Map<String, Message> getSendingMessage() {
        return sendingMessage;
    }

    public void setSendingMessage(@NonNull Map<String, Message> sendingMessage) {
        this.sendingMessage = sendingMessage;
    }

    @NonNull
    public List<MessagePersonItem> getMessages() {
        return messages;
    }

    public void setMessages(@NonNull List<MessagePersonItem> messages) {
        this.messages = messages;
    }
}
