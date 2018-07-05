package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.databinding.ActivityRegisterBinding;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.RegisterViewModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel       registerViewModel;
    private Disposable              registerDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
    }

    private void initDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = (RegisterViewModel) getLastCustomNonConfigurationInstance();
        if (registerViewModel == null) {
            registerViewModel = new RegisterViewModel();
        }
        binding.setRegisterViewModel(registerViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerViewModel.subscribeObserver(new RegisterObserver());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!registerDisposable.isDisposed()){
            registerDisposable.dispose();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return registerViewModel;
    }

    private class RegisterObserver implements Observer<Boolean> {
        @Override
        public void onSubscribe(Disposable disposable) {
            registerDisposable = disposable;
        }

        @Override
        public void onNext(Boolean isSuccess) {
            if (isSuccess) {
                finish();
            } else {
                Toast.makeText(
                        RegisterActivity.this,
                        "Username already existed!",
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
