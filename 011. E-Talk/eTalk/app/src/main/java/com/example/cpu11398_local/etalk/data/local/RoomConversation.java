package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import java.util.Map;

@Entity
public class RoomConversation {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String key;

    @NonNull
    @ColumnInfo(name = "type")
    private long type;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @NonNull
    @ColumnInfo(name = "creater")
    private String creator;

    @NonNull
    @ColumnInfo(name = "create_time")
    private Object createTime;

    @NonNull
    @ColumnInfo(name = "member")
    private Map<String, Long> members;

    @NonNull
    @ColumnInfo(name = "last_message")
    private Message lastMessage;

    public RoomConversation(@NonNull String key,
                            @NonNull long type,
                            String name,
                            String avatar,
                            @NonNull String creator,
                            @NonNull Object createTime,
                            @NonNull Map<String, Long> members,
                            @NonNull Message lastMessage) {
        this.key = key;
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.creator = creator;
        this.createTime = createTime;
        this.members = members;
        this.lastMessage = lastMessage;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @NonNull
    public long getType() {
        return type;
    }

    public void setType(@NonNull long type) {
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

    @NonNull
    public String getCreator() {
        return creator;
    }

    public void setCreator(@NonNull String creator) {
        this.creator = creator;
    }

    @NonNull
    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NonNull Object createTime) {
        this.createTime = createTime;
    }

    @NonNull
    public Map<String, Long> getMembers() {
        return members;
    }

    public void setMembers(@NonNull Map<String, Long> members) {
        this.members = members;
    }

    @NonNull
    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(@NonNull Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
