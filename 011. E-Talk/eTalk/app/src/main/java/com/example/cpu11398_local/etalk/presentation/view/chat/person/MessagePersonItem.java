package com.example.cpu11398_local.etalk.presentation.view.chat.person;

import com.example.cpu11398_local.etalk.presentation.model.Message;

public class MessagePersonItem {

    private Message message;
    private boolean isMe;
    private Object  data;
    private String  avatar;
    private int     avatarVisible;
    private String  time;
    private int     timeVisible;

    public MessagePersonItem(Message message) {
        this.message = message;
    }

    public MessagePersonItem(Message message,
                             Object data,
                             String avatar,
                             String time) {
        this.message = message;
        this.data = data;
        this.avatar = avatar;
        this.time = time;
    }

    public MessagePersonItem(Message message,
                             boolean isMe,
                             Object data,
                             String avatar,
                             int avatarVisible,
                             String time,
                             int timeVisible) {
        this.message        = message;
        this.isMe           = isMe;
        this.data           = data;
        this.avatar         = avatar;
        this.avatarVisible  = avatarVisible;
        this.time           = time;
        this.timeVisible    = timeVisible;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public Object getData() {
        return data;
    }

    public String getTextData() {
        return (String)data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAvatarVisible() {
        return avatarVisible;
    }

    public void setAvatarVisible(int avatarVisible) {
        this.avatarVisible = avatarVisible;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTimeVisible() {
        return timeVisible;
    }

    public void setTimeVisible(int timeVisible) {
        this.timeVisible = timeVisible;
    }

    public boolean equalsItems(MessagePersonItem other) {
        return (this.message.getKey())
                .equals
                (other.getMessage().getKey());
    }

    public boolean equalsContent(MessagePersonItem other) {
        if (this.timeVisible != other.timeVisible) return false;
        if (this.avatarVisible != other.avatarVisible) return false;
        if (this.avatar == null && other.avatar == null) return true;
        if (this.avatar != null && other.avatar != null
                && this.avatar.equals(other.avatar)) return true;
        return false;
    }

    public MessagePersonItem clone() {
        return new MessagePersonItem(message, isMe, data, avatar, avatarVisible, time, timeVisible);
    }
}
