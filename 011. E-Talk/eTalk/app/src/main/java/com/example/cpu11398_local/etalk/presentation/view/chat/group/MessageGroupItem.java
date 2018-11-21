package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.view.View;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import java.util.HashMap;
import java.util.Map;

public final class MessageGroupItem {

    private Message             message;
    private boolean             isMe;
    private Object              data;
    private String              name;
    private int                 nameVisible;
    private String              avatar;
    private int                 avatarVisible;
    private String              time;
    private int                 timeVisible;
    private Map<String, String> seen = new HashMap<>();
    private int                 startVisible = View.VISIBLE;
    private int                 stopVisible = View.GONE;
    private int                 progressVisible = View.GONE;
    private int                 progressPercent = 0;

    public MessageGroupItem(Message message) {
        this.message = message;
    }

    public MessageGroupItem(Message message,
                            String name,
                            Object data,
                            String avatar,
                            String time) {
        this.message    = message;
        this.name       = name;
        this.data       = data;
        this.avatar     = avatar;
        this.time       = time;
    }

    public MessageGroupItem(Message message,
                            boolean isMe,
                            String name,
                            int nameVisible,
                            Object data,
                            String avatar,
                            int avatarVisible,
                            String time,
                            int timeVisible) {
        this.message        = message;
        this.isMe           = isMe;
        this.name           = name;
        this.nameVisible    = nameVisible;
        this.data           = data;
        this.avatar         = avatar;
        this.avatarVisible  = avatarVisible;
        this.time           = time;
        this.timeVisible    = timeVisible;
    }

    public MessageGroupItem(Message message,
                            boolean isMe,
                            String name,
                            int nameVisible,
                            Object data,
                            String avatar,
                            int avatarVisible,
                            String time,
                            int timeVisible,
                            Map<String, String> seen) {
        this.message        = message;
        this.isMe           = isMe;
        this.name           = name;
        this.nameVisible    = nameVisible;
        this.data           = data;
        this.avatar         = avatar;
        this.avatarVisible  = avatarVisible;
        this.time           = time;
        this.timeVisible    = timeVisible;
        this.seen           = seen;
    }

    public MessageGroupItem(Message message,
                            boolean isMe,
                            Object data,
                            String name,
                            int nameVisible,
                            String avatar,
                            int avatarVisible,
                            String time,
                            int timeVisible,
                            Map<String, String> seen,
                            int startVisible,
                            int stopVisible,
                            int progressVisible,
                            int progressPercent) {
        this.message            = message;
        this.isMe               = isMe;
        this.data               = data;
        this.name               = name;
        this.nameVisible        = nameVisible;
        this.avatar             = avatar;
        this.avatarVisible      = avatarVisible;
        this.time               = time;
        this.timeVisible        = timeVisible;
        this.seen               = seen;
        this.startVisible       = startVisible;
        this.stopVisible        = stopVisible;
        this.progressVisible    = progressVisible;
        this.progressPercent    = progressPercent;
    }

    public Message getMessage() {
        return new Message(message);
    }

    public MessageGroupItem newMessage(Message message) {
        return new MessageGroupItem(
                message,
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public boolean isMe() {
        return isMe;
    }

    public MessageGroupItem newMe(boolean me) {
        return new MessageGroupItem(
                new Message(message),
                me,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public String getName() {
        return name;
    }

    public MessageGroupItem newName(String name) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getNameVisible() {
        return nameVisible;
    }

    public MessageGroupItem newNameVisible(int nameVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public Object getData() {
        return data;
    }

    public String getTextData() {
        return (String)data;
    }

    public MessageGroupItem newData(Object data) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public String getAvatar() {
        return avatar;
    }

    public MessageGroupItem newAvatar(String avatar) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getAvatarVisible() {
        return avatarVisible;
    }

    public MessageGroupItem newAvatarVisible(int avatarVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public String getTime() {
        return time;
    }

    public MessageGroupItem newTime(String time) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getTimeVisible() {
        return timeVisible;
    }

    public MessageGroupItem newTimeVisible(int timeVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public Map<String, String> getSeen() {
        return new HashMap<>(seen);
    }

    public MessageGroupItem newSeen(Map<String, String> seen) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getStartVisible() {
        return startVisible;
    }

    public MessageGroupItem newStartVisible(int startVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getStopVisible() {
        return stopVisible;
    }

    public MessageGroupItem newStopVisible(int stopVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getProgressVisible() {
        return progressVisible;
    }

    public MessageGroupItem newProgressVisible(int progressVisible) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public MessageGroupItem newProgressPercent(int progressPercent) {
        return new MessageGroupItem(
                new Message(message),
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                new HashMap<>(seen),
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }

    public boolean equalsItems(MessageGroupItem other) {
        return (this.message.getKey())
                .equals
                (other.getMessage().getKey());
    }

    public boolean equalsContent(MessageGroupItem other) {
        if (!this.data.equals(other.data)) return false;
        if (this.startVisible != other.startVisible) return false;
        if (this.stopVisible != other.stopVisible) return false;
        if (this.progressVisible != other.progressVisible) return false;
        if (this.progressPercent != other.progressPercent) return false;
        if (this.nameVisible != other.nameVisible) return false;
        if (this.timeVisible != other.timeVisible) return false;
        if (this.avatarVisible != other.avatarVisible) return false;
        if (this.seen.size() != other.seen.size()) return false;
        for (Map.Entry<String, String> entry : seen.entrySet()) {
            if (!other.seen.containsKey(entry.getKey())) return false;
            String thisVal  = entry.getValue();
            String otherVal = other.seen.get(entry.getKey());
            if (thisVal == null && otherVal == null) continue;
            if (!thisVal.equals(otherVal)) return false;
        }
        if (this.avatar == null && other.avatar == null) return true;
        if (this.avatar != null && other.avatar != null
                && this.avatar.equals(other.avatar)) return true;
        return false;
    }

    public MessageGroupItem clone() {
        return new MessageGroupItem(
                message,
                isMe,
                data,
                name,
                nameVisible,
                avatar,
                avatarVisible,
                time,
                timeVisible,
                seen,
                startVisible,
                stopVisible,
                progressVisible,
                progressPercent
        );
    }
}
