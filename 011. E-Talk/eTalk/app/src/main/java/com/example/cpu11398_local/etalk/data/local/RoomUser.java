package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomUser {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String username;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    @ColumnInfo(name = "phone")
    private String phone;

    @NonNull
    @ColumnInfo(name = "avatar")
    private String avatar;

    @NonNull
    @ColumnInfo(name = "active")
    private long active;

    public RoomUser(@NonNull String username,
                    @NonNull String name,
                    @NonNull String password,
                    @NonNull String phone,
                    @NonNull String avatar,
                    @NonNull long active) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.avatar = avatar;
        this.active = active;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    @NonNull
    public long getActive() {
        return active;
    }

    public void setActive(@NonNull long active) {
        this.active = active;
    }
}
