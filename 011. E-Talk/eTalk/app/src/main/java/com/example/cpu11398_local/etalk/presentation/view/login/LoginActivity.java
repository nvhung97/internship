package com.example.cpu11398_local.etalk.presentation.view.login;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityLoginBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.main.MainActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.login.LoginViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {

    @Inject
    @Named("LoginViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.login_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            MainActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((LoginViewModel)viewModel);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new LoginObserver());
    }

    @Override
    public void onUnSubscribeViewModel() {
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public Object onSaveViewModel() {
        return viewModel;
    }

    @Override
    public void onEndTaskViewModel() {
        viewModel.endTask();
    }

    @Override
    public void onBackPressed() {
        Tool.finishFailed(this);
    }

    private void onFinishSuccessfully() {
        Tool.finishSuccessfully(this);
    }

    private void onFinishFailed() {
        Tool.finishFailed(this);
    }

    private void onShowLoading() {
        dialog = Tool.createProcessingDialog(this);
        dialog.show();
    }

    private void onHideLoading() {
       if (dialog != null) {
           dialog.dismiss();
       }
    }

    private class LoginObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.LOGIN_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.LOGIN_ACTIVITY_FINISH_OK:
                    onFinishSuccessfully();
                    break;
                case Event.LOGIN_ACTIVITY_FINISH_CANCELED:
                    onFinishFailed();
                    break;
                case Event.LOGIN_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.LOGIN_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;
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
