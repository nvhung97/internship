package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.List;
import java.util.Map;

public class ContactDiffUtil extends DiffUtil.Callback{

    private User                currentUser;
    private List<Conversation>  oldConversations;
    private Map<String, User>   oldFriends;
    private List<Conversation>  newConversations;
    private Map<String, User>   newFriends;

    public ContactDiffUtil(User currentUser,
                           List<Conversation> oldConversations,
                           Map<String, User> oldFriends,
                           List<Conversation> newConversations,
                           Map<String, User> newFriends) {
        this.currentUser        = currentUser;
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
        User oldFriend = null;
        User newFriend = null;
        for (String key : oldConversations.get(oldItemPosition).getMembers().keySet()) {
            if (!key.equals(currentUser.getUsername())) {
                oldFriend = oldFriends.get(key);
                break;
            }
        }
        for (String key : newConversations.get(newItemPosition).getMembers().keySet()) {
            if (!key.equals(currentUser.getUsername())) {
                newFriend = newFriends.get(key);
                break;
            }
        }
        if (oldFriend == null && newFriend == null) {
            return true;
        }
        if (oldFriend == null && newFriend != null) {
            return false;
        }
        if (!oldFriend.getName().equals(newFriend.getName())) {
            return false;
        }
        if (oldFriend.getActive() != newFriend.getActive()) {
            return false;
        }
        if (oldFriend.getAvatar() == null & newFriend.getAvatar() == null) {
            return true;
        }
        if (oldFriend.getAvatar() == null & newFriend.getAvatar() != null) {
            return false;
        }
        if (!oldFriend.getAvatar().equals(newFriend.getAvatar())) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        User oldFriend = null;
        User newFriend = null;
        Bundle bundle  = new Bundle();
        for (String key : oldConversations.get(oldItemPosition).getMembers().keySet()) {
            if (!key.equals(currentUser.getUsername())) {
                oldFriend = oldFriends.get(key);
                break;
            }
        }
        for (String key : newConversations.get(newItemPosition).getMembers().keySet()) {
            if (!key.equals(currentUser.getUsername())) {
                newFriend = newFriends.get(key);
                break;
            }
        }
        bundle.putString("Key", newFriend.getUsername());
        if (oldFriend == null && newFriend != null) {
            bundle.putString("avatar", newFriend.getAvatar());
            bundle.putString("name", newFriend.getName());
            bundle.putLong("active", newFriend.getActive());
        }
        if (oldFriend != null && newFriend != null) {
            if (!oldFriend.getName().equals(newFriend.getName())) {
                bundle.putString("name", newFriend.getName());
            }
            if (oldFriend.getActive() != newFriend.getActive()) {
                bundle.putLong("active", newFriend.getActive());
            }
            if ((oldFriend.getAvatar() == null && newFriend.getAvatar() != null)
                    || (oldFriend.getAvatar() != null && newFriend.getAvatar() == null)) {
                bundle.putString("avatar", newFriend.getAvatar());
            }
            if (oldFriend.getAvatar() != null && newFriend.getAvatar() != null
                    && !oldFriend.getAvatar().equals(newFriend.getAvatar())) {
                bundle.putString("avatar", newFriend.getAvatar());
            }
        }
        return bundle;
    }
}
