package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.databinding.ActivityRegisterBinding;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.RegisterViewModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.ViewModel;

import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity {

    @Inject
    public  RegisterViewModel       registerViewModel;

    private ActivityRegisterBinding binding;
    private Disposable              registerDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = (RegisterViewModel) getLastCustomNonConfigurationInstance();
        if (registerViewModel == null) {
            ContentActivity.getAppComponent(this).inject(this);
        }
        binding.setRegisterViewModel(registerViewModel);
    }

    @Override
    void onSubscribeViewModel() {
        registerViewModel.subscribeObserver(new RegisterObserver());
    }

    @Override
    void onUnSubscribeViewModel() {
        if (!registerDisposable.isDisposed()){
            registerDisposable.dispose();
        }
    }

    @Override
    Object onSaveViewModel() {
        return registerViewModel;
    }

    @Override
    void onEndTaskViewModel() {
        registerViewModel.endTask();
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
