package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts;

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
        return (oldConversations.get(oldItemPosition).getCreator() + oldConversations.get(oldItemPosition).getTime())
                .equals(newConversations.get(newItemPosition).getCreator() + newConversations.get(newItemPosition).getTime());
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
        if ((oldFriend == null && newFriend == null)
                || (oldFriend != null && newFriend != null
                    && oldFriend.getName().equals(newFriend.getName())
                    && oldFriend.getAvatar().equals(newFriend.getAvatar())
                    && oldFriend.getActive() == newFriend.getActive())) {
            return true;
        }
        return false;
    }
}
