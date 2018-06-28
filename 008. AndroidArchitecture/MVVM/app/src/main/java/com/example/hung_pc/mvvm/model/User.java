package com.example.hung_pc.mvvm.model;

import com.example.hung_pc.mvvm.utils.Utils;

/**
 * Created by Hung-pc on 6/28/2018.
 */

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