package com.example.cpu11398_local.cleanarchitecturedemo.data.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import io.reactivex.Single;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public class SharedPreferencesManager implements CacheSource {

    private final String FILENAME = "user_loging_in";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String DEFAULT  = "default";

    private SharedPreferences        sharedPref;
    private SharedPreferences.Editor editor;

    @Inject
    public SharedPreferencesManager(Context context) {
        sharedPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public Single<User> getUser() {
        return Single.just(
                new User(
                        sharedPref.getString(USERNAME, DEFAULT),
                        sharedPref.getString(PASSWORD, DEFAULT)
                )
        );
    }

    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
    @Override
    public void putUser(Optional<User> user) {
        if (user.isPresent()) {
            editor.putString(USERNAME, user.get().getUsername());
            editor.putString(PASSWORD, user.get().getPassword());
            editor.commit();
        }
    }
}
