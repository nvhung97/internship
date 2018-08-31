package com.example.cpu11398_local.etalk.presentation.model;

import java.util.Map;

public class User {

    private String              name;
    private String              username;
    private String              password;
    private String              phone;
    private String              avatar;
    private long                active;
    private Map<String, String> friends;
    private Map<String, String> conversations;

    public User() {
    }

    public User(String name,
                String username,
                String password,
                String phone) {
        this(name, username, password, phone, null);
    }

    public User(String name,
                String username,
                String password,
                String phone,
                String avatar) {
        this(name, username, password, phone, avatar, System.currentTimeMillis(), null, null);
    }

    public User(String name,
                String username,
                String password,
                String phone,
                String avatar,
                long active,
                Map<String, String> friends,
                Map<String, String> conversations) {
        this.name           = name;
        this.username       = username;
        this.password       = password;
        this.phone          = phone;
        this.avatar         = avatar;
        this.active         = active;
        this.friends        = friends;
        this.conversations  = conversations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, String> friends) {
        this.friends = friends;
    }

    public Map<String, String> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, String> conversations) {
        this.conversations = conversations;
    }
}
