package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import java.util.List;
import java.util.Map;

@Entity
public class RoomMessagesGroupHolder {

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
    List<MessageGroupItem> messages;

    public RoomMessagesGroupHolder(@NonNull String conversationKey,
                                   @NonNull MessagesGroupHolder holder) {
        this.conversationKey = conversationKey;
        this.rawMessages     = holder.getRawMessages();
        this.sendingMessage  = holder.getSendingMessage();
        this.messages        = holder.getMessages();
    }

    public RoomMessagesGroupHolder(@NonNull String conversationKey,
                                   @NonNull Map<String, Message> rawMessages,
                                   @NonNull Map<String, Message> sendingMessage,
                                   @NonNull List<MessageGroupItem> messages) {
        this.conversationKey    = conversationKey;
        this.rawMessages        = rawMessages;
        this.sendingMessage     = sendingMessage;
        this.messages           = messages;
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
    public List<MessageGroupItem> getMessages() {
        return messages;
    }

    public void setMessages(@NonNull List<MessageGroupItem> messages) {
        this.messages = messages;
    }
}
