package com.example.cpu11398_local.etalk.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.cpu11398_local.etalk.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import javax.inject.Inject;
import io.reactivex.Single;

public class SharedPreferencesDB implements CacheSource {

    private final String FILENAME   = "etalk_cache";
    private final String NAME       = "name";
    private final String USERNAME   = "username";
    private final String PASSWORD   = "password";
    private final String PHONE      = "phone";
    private final String STATUS     = "status";
    private final String DEFAULT    = "default";

    private SharedPreferences   sharedPref;
    private Editor              editor;

    @Inject
    public SharedPreferencesDB(Context context) {
        sharedPref  = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        editor      = sharedPref.edit();
    }

    @Override
    public Single<User> getUser() {
        return Single.just(
                new User(
                        sharedPref.getString(NAME, DEFAULT),
                        sharedPref.getString(USERNAME, DEFAULT),
                        sharedPref.getString(PASSWORD, DEFAULT),
                        sharedPref.getString(PHONE, DEFAULT),
                        sharedPref.getString(STATUS, DEFAULT)
                )
        );
    }

    @Override
    public void cacheUser(User user) {
        if (user != null) {
            editor.putString(NAME, user.getName());
            editor.putString(USERNAME, user.getUsername());
            editor.putString(PASSWORD, user.getPassword());
            editor.putString(PHONE, user.getPhone());
            editor.putString(STATUS, user.getStatus());
            editor.commit();
        } else {
            editor.remove(NAME);
            editor.remove(USERNAME);
            editor.remove(PASSWORD);
            editor.remove(PHONE);
            editor.remove(STATUS);
            editor.commit();
        }
    }

    @Override
    public Single<Boolean> checkUserLoggedIn() {
        return Single.just(
                !sharedPref.getString(USERNAME, DEFAULT).equals(DEFAULT)
        );
    }
}
