package com.example.cpu11398_local.etalk.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.cpu11398_local.etalk.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;

public class SharedPreferencesDB implements CacheSource {

    private final String FILENAME   = FirebaseTree.NODE_NAME;
    private final String NAME       = FirebaseTree.Users.Name.NODE_NAME;
    private final String USERNAME   = FirebaseTree.Users.Username.NODE_NAME;
    private final String PASSWORD   = FirebaseTree.Users.Password.NODE_NAME;
    private final String PHONE      = FirebaseTree.Users.Phone.NODE_NAME;
    private final String AVATAR     = FirebaseTree.Users.Avatar.NODE_NAME;

    private SharedPreferences       sharedPref;
    private Editor                  editor;
    private ObservableEmitter<User> changeableUserEmitter;

    @Inject
    public SharedPreferencesDB(Context context) {
        sharedPref  = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        editor      = sharedPref.edit();
    }

    @Override
    public Single<User> getUser() {
        return Single.just(getUserHelper());
    }

    @Override
    public Observable<User> getChangeableUser(){
        return Observable.create(emitter -> {
            changeableUserEmitter = emitter;
            changeableUserEmitter.onNext(getUserHelper());
        });
    }

    @Override
    public void cacheUser(User user) {
        if (user != null) {
            editor.putString(NAME, user.getName());
            editor.putString(USERNAME, user.getUsername());
            editor.putString(PASSWORD, user.getPassword());
            editor.putString(PHONE, user.getPhone());
            editor.putString(AVATAR, user.getAvatar());
            editor.commit();
            if (changeableUserEmitter != null) {
                changeableUserEmitter.onNext(user);
            }
        } else {
            editor.remove(NAME);
            editor.remove(USERNAME);
            editor.remove(PASSWORD);
            editor.remove(PHONE);
            editor.remove(AVATAR);
            editor.commit();
        }
    }

    private User getUserHelper() {
        return new User(
                sharedPref.getString(NAME, ""),
                sharedPref.getString(USERNAME, ""),
                sharedPref.getString(PASSWORD, ""),
                sharedPref.getString(PHONE, ""),
                sharedPref.getString(AVATAR, null)
        );
    }

    @Override
    public Single<String> getUsernameLoggedIn() {
        return Single.just(
                sharedPref.getString(USERNAME, "")
        );
    }
}
