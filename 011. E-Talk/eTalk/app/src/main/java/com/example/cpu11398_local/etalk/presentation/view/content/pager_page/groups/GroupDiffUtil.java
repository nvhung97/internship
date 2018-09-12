package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupDiffUtil extends DiffUtil.Callback{

    private List<Conversation>  oldConversations;
    private Map<String, User>   oldFriends;
    private List<Conversation>  newConversations;
    private Map<String, User>   newFriends;

    public GroupDiffUtil(List<Conversation> oldConversations,
                         Map<String, User> oldFriends,
                         List<Conversation> newConversations,
                         Map<String, User> newFriends) {
        this.oldConversations   = oldConversations;
        this.oldFriends         = oldFriends;
        this.newConversations   = newConversations;
        this.newFriends         = newFriends;
    }

    @Override
    public int getOldListSize() {
        return oldConversations.size();
    }

    @Override
    public int getNewListSize() {
        return newConversations.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldConversations.get(oldItemPosition).getCreator() + oldConversations.get(oldItemPosition).getTime())
                .equals(newConversations.get(newItemPosition).getCreator() + newConversations.get(newItemPosition).getTime());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Conversation oldConversation = oldConversations.get(oldItemPosition);
        Conversation newConversation = newConversations.get(newItemPosition);
        if (!(oldConversation.getLastMessage().getSender() + oldConversation.getLastMessage().getTime())
                .equals((newConversation.getLastMessage().getSender() + newConversation.getLastMessage().getTime()))) {
            return false;
        }
        if (!newConversation.getName().equals(newConversation.getName())) {
            return false;
        }
        if (oldConversation.getAvatar() == null && newConversation.getAvatar() != null) {
            return false;
        } else if (oldConversation.getAvatar() != null) {
            return oldConversation.getAvatar().equals(newConversation.getAvatar());
        } else {
            List<String> oldKeyFriends = new ArrayList<>(oldConversation.getMembers().keySet());
            List<String> newKeyFriends = new ArrayList<>(newConversation.getMembers().keySet());
            if (!((oldFriends.get(oldKeyFriends.get(0)) == null && newFriends.get(newKeyFriends.get(0)) == null)
                    || (oldFriends.get(oldKeyFriends.get(0)) != null && newFriends.get(newKeyFriends.get(0)) != null
                    && oldFriends.get(oldKeyFriends.get(0)).getAvatar().equals(newFriends.get(newKeyFriends.get(0)).getAvatar())))) {
                return false;
            }
            if (!((oldFriends.get(oldKeyFriends.get(1)) == null && newFriends.get(newKeyFriends.get(1)) == null)
                    || (oldFriends.get(oldKeyFriends.get(1)) != null && newFriends.get(newKeyFriends.get(1)) != null
                    && oldFriends.get(oldKeyFriends.get(1)).getAvatar().equals(newFriends.get(newKeyFriends.get(1)).getAvatar())))) {
                return false;
            }
            if (!((oldFriends.get(oldKeyFriends.get(2)) == null && newFriends.get(newKeyFriends.get(2)) == null)
                    || (oldFriends.get(oldKeyFriends.get(2)) != null && newFriends.get(newKeyFriends.get(2)) != null
                    && oldFriends.get(oldKeyFriends.get(2)).getAvatar().equals(newFriends.get(newKeyFriends.get(2)).getAvatar())))) {
                return false;
            }
        }
        if (oldConversation.getMembers().size() != newConversation.getMembers().size()) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Conversation oldConversation = oldConversations.get(oldItemPosition);
        Conversation newConversation = newConversations.get(newItemPosition);
        Bundle bundle  = new Bundle();
        if (!(oldConversation.getLastMessage().getSender() + oldConversation.getLastMessage().getTime())
                .equals((newConversation.getLastMessage().getSender() + newConversation.getLastMessage().getTime()))) {
            bundle.putString("data", newConversation.getLastMessage().getData());
            bundle.putLong("time", newConversation.getLastMessage().getTime());
        }
        if (!newConversation.getName().equals(newConversation.getName())) {
            bundle.putString("name", newConversation.getName());
        }
        if (oldConversation.getAvatar() == null && newConversation.getAvatar() != null) {
            bundle.putString("avatar", newConversation.getAvatar());
        } else if (oldConversation.getAvatar() != null && !oldConversation.getAvatar().equals(newConversation.getAvatar())) {
            bundle.putString("avatar", newConversation.getAvatar());
        } else {
            List<String> oldKeyFriends = new ArrayList<>(oldConversation.getMembers().keySet());
            List<String> newKeyFriends = new ArrayList<>(newConversation.getMembers().keySet());
            if (!((oldFriends.get(oldKeyFriends.get(0)) == null && newFriends.get(newKeyFriends.get(0)) == null)
                    || (oldFriends.get(oldKeyFriends.get(0)) != null && newFriends.get(newKeyFriends.get(0)) != null
                    && oldFriends.get(oldKeyFriends.get(0)).getAvatar().equals(newFriends.get(newKeyFriends.get(0)).getAvatar())))) {
                bundle.putString("avatar1", newFriends.get(newKeyFriends.get(0)).getAvatar());
            }
            if (!((oldFriends.get(oldKeyFriends.get(1)) == null && newFriends.get(newKeyFriends.get(1)) == null)
                    || (oldFriends.get(oldKeyFriends.get(1)) != null && newFriends.get(newKeyFriends.get(1)) != null
                    && oldFriends.get(oldKeyFriends.get(1)).getAvatar().equals(newFriends.get(newKeyFriends.get(1)).getAvatar())))) {
                bundle.putString("avatar2", newFriends.get(newKeyFriends.get(1)).getAvatar());
            }
            if (!((oldFriends.get(oldKeyFriends.get(2)) == null && newFriends.get(newKeyFriends.get(2)) == null)
                    || (oldFriends.get(oldKeyFriends.get(2)) != null && newFriends.get(newKeyFriends.get(2)) != null
                    && oldFriends.get(oldKeyFriends.get(2)).getAvatar().equals(newFriends.get(newKeyFriends.get(2)).getAvatar())))) {
                bundle.putString("avatar3", newFriends.get(newKeyFriends.get(2)).getAvatar());
            }
        }
        if (oldConversation.getMembers().size() != newConversation.getMembers().size()) {
            bundle.putLong("number", newConversation.getMembers().size());
        }
        return bundle;
    }
}
