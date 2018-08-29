package com.example.cpu11398_local.etalk.presentation.view.register;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityRegisterBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.register.RegisterViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity {

    @Inject
    @Named("RegisterViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.register_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((RegisterViewModel)viewModel);
        addControlKeyboardView(
                binding.registerActivityEdtName,
                binding.registerActivityEdtUsername,
                binding.registerActivityEdtPassword,
                binding.registerActivityBtnPassword,
                binding.registerActivityEdtPhone
        );
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new RegisterObserver());
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

    private class RegisterObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.REGISTER_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.REGISTER_ACTIVITY_FINISH_OK:
                    onFinishSuccessfully();
                    break;
                case Event.REGISTER_ACTIVITY_FINISH_CANCELED:
                    onFinishFailed();
                    break;
                case Event.REGISTER_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.REGISTER_ACTIVITY_HIDE_LOADING:
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
