package com.example.cpu11398_local.dagger2demo.DI;

import com.example.cpu11398_local.dagger2demo.data.User;
import com.example.cpu11398_local.dagger2demo.data.UserManager;
import com.example.cpu11398_local.dagger2demo.utils.Utils;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final String TAG = "AppModule";

    @Provides
    public User provideUser() {
        Utils.showLog(TAG, "provideUser");
        return new User("username", "password");
    }

    @Provides
    @Singleton
    public UserManager provideUserManager(User user) {
        Utils.showLog(TAG, "provideUserManager(" + user.toString() + ")");
        return new UserManager(user);
    }
}
