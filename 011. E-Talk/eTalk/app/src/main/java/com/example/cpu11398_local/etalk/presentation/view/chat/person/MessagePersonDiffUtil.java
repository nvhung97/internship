package com.example.cpu11398_local.etalk.presentation.view.chat.person;

import android.support.v7.util.DiffUtil;
import java.util.List;

public class MessagePersonDiffUtil extends DiffUtil.Callback {

    private List<MessagePersonItem> oldMessages;
    private List<MessagePersonItem> newMessages;

    public MessagePersonDiffUtil(List<MessagePersonItem> oldMessages,
                                 List<MessagePersonItem> newMessages) {
        this.oldMessages = oldMessages;
        this.newMessages = newMessages;
    }

    @Override
    public int getOldListSize() {
        return oldMessages.size();
    }

    @Override
    public int getNewListSize() {
        return newMessages.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).equalsItems(newMessages.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).equalsContent(newMessages.get(newItemPosition));
    }
}
