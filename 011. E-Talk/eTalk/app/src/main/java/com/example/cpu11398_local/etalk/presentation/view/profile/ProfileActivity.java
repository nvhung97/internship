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
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.disposables.Disposable;

public class ProfileActivity extends BaseActivity {

    //@Inject
    //@Named("ProfileViewModel")
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
            //WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ProfileViewModel) viewModel);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void onUnSubscribeViewModel() {

    }

    @Override
    public Object onSaveViewModel() {
        return null;
    }

    @Override
    public void onEndTaskViewModel() {

    }
}
