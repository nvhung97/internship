package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.support.v7.util.DiffUtil;
import java.util.List;

public class MessageGroupDiffUtil extends DiffUtil.Callback{

    private List<MessageGroupItem> oldMessages;
    private List<MessageGroupItem> newMessages;

    public MessageGroupDiffUtil(List<MessageGroupItem> oldMessages,
                                List<MessageGroupItem> newMessages) {
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
