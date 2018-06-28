package com.example.cpu11398_local.mvp.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.SystemClock;
import com.example.cpu11398_local.mvp.model.User;
import com.example.cpu11398_local.mvp.utils.Utils;
import com.example.cpu11398_local.mvp.view.ILoginView;

public class LoginPresenter implements ILoginPresenter {

    private final String TAG = "LoginPresenter";

    private ILoginView loginView;
    private User       user;

    public LoginPresenter(ILoginView loginView) {
        Utils.showLog(TAG, "LoginPresenter");
        this.loginView = loginView;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void performLogin(final String username, final String password) {
        Utils.showLog(TAG, "performLogin");
        new AsyncTask<Void, Void, Void> () {
            @Override
            protected void onPreExecute() {
                Utils.showLog(TAG, "performLogin.onPreExecute");
                checkEmptyField(username, password);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Utils.showLog(TAG, "performLogin.doInBackground");
                loadData();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Utils.showLog(TAG, "performLogin.onPostExecute");
                checkLogin(username, password);
            }
        }.execute();
    }

    private void checkEmptyField(String username, String password) {
        Utils.showLog(TAG, "checkEmptyField");
        if (username.isEmpty()) {
            loginView.onUsernameEmpty();
        }
        if (password.isEmpty()) {
            loginView.onPasswordEmpty();
        }
    }

    private void loadData() {
        Utils.showLog(TAG, "loadData");
        if (user == null) {
            SystemClock.sleep(5000);
            user = new User();
        }
    }

    private void checkLogin(String username, String password) {
        Utils.showLog(TAG, "checkLogin");
        if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
            loginView.onLoginSuccess();
        } else {
            loginView.onLoginFailed();
        }
    }
}
