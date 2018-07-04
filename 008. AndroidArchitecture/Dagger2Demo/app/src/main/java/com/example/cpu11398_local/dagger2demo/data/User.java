package com.example.cpu11398_local.dagger2demo.data;

import com.example.cpu11398_local.dagger2demo.utils.Utils;

public class User {

    private final String TAG = "User";

    private String username;
    private String password;

    public User(String defaultUsername, String defaultPassword) {
        Utils.showLog(TAG, "User(" + defaultUsername + ", "  + defaultPassword + ")");
        this.username = defaultUsername;
        this.password = defaultPassword;
    }

    public String getUsername() {
        Utils.showLog(TAG, "getUsername");
        return username;
    }

    public void setUsername(String username) {
        Utils.showLog(TAG, "setUsername(" + username + ")");
        this.username = username;
    }

    public String getPassword() {
        Utils.showLog(TAG, "getPassword");
        return password;
    }

    public void setPassword(String password) {
        Utils.showLog(TAG, "setPassword(" + password + ")");
        this.password = password;
    }

    public String toString() {
        return "{username: " + username + ", password: " + password + "}";
    }
}
