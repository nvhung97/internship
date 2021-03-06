package com.example.hung_pc.mvvm.view_model;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import com.example.hung_pc.mvvm.model.User;
import com.example.hung_pc.mvvm.utils.Utils;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Hung-pc on 6/28/2018.
 */

public class ViewModel {

    private final String TAG = "ViewModel";

    private User user;

    public PublishSubject<Boolean>   publishSubject  = PublishSubject.create();
    public ObservableField<Boolean>  isUsernameEmpty = new ObservableField<>(false);
    public ObservableField<Boolean>  isPasswordEmpty = new ObservableField<>(false);
    public ObservableField<String>   username        = new ObservableField<>("");
    public ObservableField<String>   password        = new ObservableField<>("");

    public ViewModel(){
        Utils.showLog(TAG, "ViewModel");
    }

    public void subscribeObserver(Observer<Boolean> observer) {
        publishSubject.subscribe(observer);
    }

    @SuppressLint("StaticFieldLeak")
    public void performLogin(View view){
        Utils.showLog(TAG, "performLogin");
        if (!isExistEmptyField()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Utils.showLog(TAG, "performLogin.doInBackground");
                    if (user == null) {
                        loadData();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showLog(TAG, "performLogin.onPostExecute");
                    checkLogin();
                }
            }.execute();
        }
    }

    private boolean isExistEmptyField() {
        Utils.showLog(TAG, "isExistEmptyField");
        boolean result = false;
        if (username.get().isEmpty()) {
            isUsernameEmpty.set(true);
            result = true;
        }
        if (password.get().isEmpty()) {
            isPasswordEmpty.set(true);
            result = true;
        }
        return result;
    }

    private void loadData() {
        Utils.showLog(TAG, "loadData");
        SystemClock.sleep(1000);
        user = new User();
    }

    private void checkLogin() {
        Utils.showLog(TAG, "checkLogin");
        if (username.get().equals(user.getUsername()) && password.get().equals(user.getPassword())) {
            publishSubject.onNext(true);
        } else {
            publishSubject.onNext(false);
        }
    }
}
