package com.example.cpu11398_local.etalk.presentation.model;

import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ServerValue;
import java.util.Map;

public class Conversation {

    public static final long GROUP  = 0;
    public static final long PERSON = 1;

    private String              key;
    private long                type;
    private String              name;
    private String              avatar;
    private String              creator;
    private Object              createTime;
    private Map<String, Long>   members;
    private Message             lastMessage;

    public Conversation() {
    }

    public Conversation(long type,
                        String name,
                        String avatar,
                        String creator,
                        Map<String, Long> members,
                        Message lastMessage) {
        this(creator + System.currentTimeMillis(), type, name, avatar, creator, ServerValue.TIMESTAMP, members, lastMessage);
    }

    public Conversation(String key,
                        long type,
                        String name,
                        String avatar,
                        String creator,
                        Object createTime,
                        Map<String, Long> members,
                        Message lastMessage) {
        this.key         = key;
        this.type        = type;
        this.name        = name;
        this.avatar      = avatar;
        this.creator     = creator;
        this.createTime  = createTime;
        this.members     = members;
        this.lastMessage = lastMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Exclude
    public long getTime() {
        return (long)createTime;
    }

    @PropertyName(FirebaseTree.Database.Conversations.ConversationKey.CreateTime.NODE_NAME)
    public Object getServerTime() {
        return createTime;
    }

    @PropertyName(FirebaseTree.Database.Conversations.ConversationKey.CreateTime.NODE_NAME)
    public void setServerTime(Object createTime) {
        this.createTime = createTime;
    }

    public Map<String, Long> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Long> members) {
        this.members = members;
    }

    @PropertyName(FirebaseTree.Database.Conversations.ConversationKey.LastMessage.NODE_NAME)
    public Message getLastMessage() {
        return lastMessage;
    }

    @PropertyName(FirebaseTree.Database.Conversations.ConversationKey.LastMessage.NODE_NAME)
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
