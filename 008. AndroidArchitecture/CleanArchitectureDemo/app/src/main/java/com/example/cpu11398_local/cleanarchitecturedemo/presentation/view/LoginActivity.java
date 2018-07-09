package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.databinding.ActivityLoginBinding;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.LoginViewModel;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {

    @Inject
    public  LoginViewModel       loginViewModel;

    private ActivityLoginBinding binding;
    private Disposable           loginDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = (LoginViewModel) getLastCustomNonConfigurationInstance();
        if (loginViewModel == null) {
            ContentActivity.getAppComponent(this).inject(this);
        }
        binding.setLoginViewModel(loginViewModel);
    }

    @Override
    void onSubscribeViewModel() {
        loginViewModel.subscribeObserver(new LoginObserver());
    }

    @Override
    void onUnSubscribeViewModel() {
        if (!loginDisposable.isDisposed()){
            loginDisposable.dispose();
        }
    }

    @Override
    Object onSaveViewModel() {
        return loginViewModel;
    }

    @Override
    void onEndTaskViewModel() {
        loginViewModel.endTask();
    }

    public void showRegisterView(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private class LoginObserver implements Observer<Boolean> {
        @Override
        public void onSubscribe(Disposable disposable) {
            loginDisposable = disposable;
        }

        @Override
        public void onNext(Boolean isSuccess) {
            if (isSuccess) {
                startActivity(new Intent(LoginActivity.this, ContentActivity.class));
            } else {
                Toast.makeText(
                        LoginActivity.this,
                        "Username or Password is incorrect!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
