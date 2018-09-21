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
        return (oldConversations.get(oldItemPosition).getKey())
                .equals(newConversations.get(newItemPosition).getKey());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Conversation oldConversation = oldConversations.get(oldItemPosition);
        Conversation newConversation = newConversations.get(newItemPosition);
        if (!(oldConversation.getLastMessage().getKey())
                .equals((newConversation.getLastMessage().getKey()))) {
            return false;
        }
        if (!oldConversation.getName().equals(newConversation.getName())) {
            return false;
        }
        if (oldConversation.getAvatar() == null && newConversation.getAvatar() != null) {
            return false;
        } else if (oldConversation.getAvatar() != null) {
            return oldConversation.getAvatar().equals(newConversation.getAvatar());
        } else {
            List<String> oldKeyFriends = new ArrayList<>(oldConversation.getMembers().keySet());
            List<String> newKeyFriends = new ArrayList<>(newConversation.getMembers().keySet());
            for (int i = 1; i <= 3; ++i) {
                User oldFriend = oldFriends.get(oldKeyFriends.get(i - 1));
                User newFriend = newFriends.get(newKeyFriends.get(i - 1));
                if (oldFriend == null && newFriend != null) {
                    return false;
                }
                if (oldFriend != null && newFriend != null) {
                    if ((oldFriend.getAvatar() == null && newFriend.getAvatar() != null)
                        || (oldFriend.getAvatar() != null && newFriend.getAvatar() == null)) {
                        return false;
                    }
                    if (oldFriend.getAvatar() != null && newFriend.getAvatar() != null
                            && !oldFriend.getAvatar().equals(newFriend.getAvatar())) {
                        return false;
                    }
                }
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
        if (!(oldConversation.getLastMessage().getKey())
                .equals((newConversation.getLastMessage().getKey()))) {
            bundle.putString("data", newConversation.getLastMessage().getData());
            bundle.putLong("type", newConversation.getLastMessage().getType());
            bundle.putLong("time", newConversation.getLastMessage().getTime());
        }
        if (!oldConversation.getName().equals(newConversation.getName())) {
            bundle.putString("name", newConversation.getName());
        }
        if (oldConversation.getAvatar() == null && newConversation.getAvatar() != null) {
            bundle.putString("avatar", newConversation.getAvatar());
        } else if (oldConversation.getAvatar() != null && !oldConversation.getAvatar().equals(newConversation.getAvatar())) {
            bundle.putString("avatar", newConversation.getAvatar());
        } else {
            List<String> oldKeyFriends = new ArrayList<>(oldConversation.getMembers().keySet());
            List<String> newKeyFriends = new ArrayList<>(newConversation.getMembers().keySet());
            for (int i = 1; i <= 3; ++i) {
                User oldFriend = oldFriends.get(oldKeyFriends.get(i - 1));
                User newFriend = newFriends.get(newKeyFriends.get(i - 1));
                if (oldFriend == null && newFriend != null) {
                    bundle.putString("avatar" + i, newFriend.getAvatar());
                }
                if (oldFriend != null && newFriend != null) {
                    if ((oldFriend.getAvatar() == null && newFriend.getAvatar() != null)
                            || (oldFriend.getAvatar() != null && newFriend.getAvatar() == null)) {
                        bundle.putString("avatar" + i, newFriend.getAvatar());
                    }
                    if (oldFriend.getAvatar() != null && newFriend.getAvatar() != null
                            && !oldFriend.getAvatar().equals(newFriend.getAvatar())) {
                        bundle.putString("avatar" + i, newFriend.getAvatar());
                    }
                }
            }
        }
        if (oldConversation.getMembers().size() != newConversation.getMembers().size()) {
            bundle.putLong("number", newConversation.getMembers().size());
        }
        return bundle;
    }
}
