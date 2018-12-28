package com.example.cpu11398_local.etalk.presentation.view.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityProfileBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.camera.CaptureActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.ContentActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.profile.ProfileViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ProfileActivity extends BaseActivity {

    private final int REQUEST_CAMERA_CODE  = 0;
    private final int REQUEST_GALLERY_CODE = 1;

    @Inject
    @Named("ProfileViewModel")
    public ViewModel    viewModel;

    private Disposable                  disposable;
    private Dialog                      dialog;
    private ProfileViewModel.AvatarCopy avatarCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.profile_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ProfileViewModel)viewModel);
        addControlKeyboardView(
                binding.profileActivityEdtName,
                binding.profileActivityEdtUsername,
                binding.profileActivityEdtPassword,
                binding.profileActivityBtnPassword,
                binding.profileActivityEdtPhone
        );
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

    private void onShowLoading() {
        dialog = Tool.createProcessingDialog(this);
        dialog.show();
    }

    private void onHideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
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
                case Event.PROFILE_ACTIVITY_SHOW_IMAGE_OPTION:
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                ProfileActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA
                                },
                                0
                        );
                    } else {
                        avatarCopy = (ProfileViewModel.AvatarCopy)data[0];
                        Tool.createImageOptionDialog(
                                ProfileActivity.this,
                                REQUEST_CAMERA_CODE,
                                REQUEST_GALLERY_CODE
                        ).show();
                    }
                    break;
                case Event.PROFILE_ACTIVITY_TIME_OUT:
                    Toast.makeText(
                            ProfileActivity.this,
                            getString(R.string.profile_activity_request_timeout),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case Event.PROFILE_ACTIVITY_UPDATE_OK:
                    Toast.makeText(
                            ProfileActivity.this,
                            getString(R.string.profile_activity_update_successfully),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case Event.PROFILE_ACTIVITY_UPDATE_FAILED:
                    Toast.makeText(
                            ProfileActivity.this,
                            getString(R.string.profile_activity_update_failed),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case Event.PROFILE_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;
                case Event.PROFILE_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.PROFILE_ACTIVITY_BACK:
                    onBackPressed();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA_CODE) {
                Bitmap bitmapAvatar = Tool.getImageWithUri(this, data.getData());
                avatarCopy.copy(bitmapAvatar);
            }
            else if (requestCode == REQUEST_GALLERY_CODE) {
                Bitmap bitmapAvatar = Tool.getImageWithUri(this, data.getData());
                avatarCopy.copy(bitmapAvatar);
            }
        }
    }
}
