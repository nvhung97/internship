package com.example.cpu11398_local.mvvm.model;

import com.example.cpu11398_local.mvvm.utils.Utils;

public class User {

    private final String TAG = "Model";

    private String username;
    private String password;

    public User() {
        Utils.showLog(TAG, "User");
        this.username = "username";
        this.password = "password";
    }

    public String getUsername() {
        Utils.showLog(TAG, "getUsername");
        return username;
    }

    public String getPassword() {
        Utils.showLog(TAG, "getPassword");
        return password;
    }
}
