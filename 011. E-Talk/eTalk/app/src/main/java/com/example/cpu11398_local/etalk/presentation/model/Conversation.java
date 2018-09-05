package com.example.cpu11398_local.etalk.presentation.model;

import com.google.firebase.database.PropertyName;
import java.util.Map;

public class Conversation {

    public static final long GROUP  = 0;
    public static final long PERSON = 1;

    private long                type;
    private String              name;
    private String              avatar;
    private String              creator;
    private long                time;
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
        this(type, name, avatar, creator, System.currentTimeMillis(), members, lastMessage);
    }

    public Conversation(long type,
                        String name,
                        String avatar,
                        String creator,
                        long time,
                        Map<String, Long> members,
                        Message lastMessage) {
        this.type        = type;
        this.name        = name;
        this.avatar      = avatar;
        this.creator     = creator;
        this.time        = time;
        this.members     = members;
        this.lastMessage = lastMessage;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Long> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Long> members) {
        this.members = members;
    }

    @PropertyName("last_message")
    public Message getLastMessage() {
        return lastMessage;
    }

    @PropertyName("last_message")
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
