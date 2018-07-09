package com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.model;

public class PaperUser {

    private String username;
    private String password;

    public PaperUser() {}

    public PaperUser(String username, String password) {
        this.username = username;
        this.password = password;
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
}
