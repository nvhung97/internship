package com.example.cpu11398_local.etalk.presentation.view.profile;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityProfileBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.profile.ProfileViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ProfileActivity extends BaseActivity {

    @Inject
    @Named("ProfileViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDataBinding() {
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ProfileViewModel)viewModel);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new ProfileObserver());
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

    private class ProfileObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                /*case Event.LOGIN_ACTIVITY_BACK:
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
                    break;*/
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
