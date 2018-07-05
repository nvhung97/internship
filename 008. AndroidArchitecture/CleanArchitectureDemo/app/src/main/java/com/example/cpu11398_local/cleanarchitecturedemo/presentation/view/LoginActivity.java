package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.databinding.ActivityLoginBinding;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.LoginViewModel;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel       loginViewModel;
    private Disposable           loginDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
    }

    private void initDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = (LoginViewModel) getLastCustomNonConfigurationInstance();
        if (loginViewModel == null) {
            loginViewModel = new LoginViewModel();
        }
        binding.setLoginViewModel(loginViewModel);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginViewModel.subscribeObserver(new LoginObserver());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!loginDisposable.isDisposed()){
            loginDisposable.dispose();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return loginViewModel;
    }

    @OnClick(R.id.btn_login_create)
    public void showRegisterView() {
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
