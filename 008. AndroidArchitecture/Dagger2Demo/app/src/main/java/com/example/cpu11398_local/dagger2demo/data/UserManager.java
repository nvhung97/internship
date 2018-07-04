package com.example.cpu11398_local.dagger2demo.data;

import com.example.cpu11398_local.dagger2demo.utils.Utils;
import javax.inject.Inject;

public class UserManager {

    private final String TAG = "UserManager";

    private User user;

    @Inject
    public UserManager(User user) {
        Utils.showLog(TAG, "UserManager(" + user.toString() + ")");
        this.user = user;
    }

    public void login(String username, String password) {
        Utils.showLog(TAG, "login(" + username + ", "  + password + ")");
        if (username.equals(user.getUsername()) &&  password.equals(user.getPassword())) {
            Utils.showLog(TAG, "login.successfully");
        }
        else {
            Utils.showLog(TAG, "login.failed");
        }
    }

    public void change(String username, String password) {
        Utils.showLog(TAG, "change(" + username + ", "  + password + ")");
        user.setUsername(username);
        user.setPassword(password);
    }
}
