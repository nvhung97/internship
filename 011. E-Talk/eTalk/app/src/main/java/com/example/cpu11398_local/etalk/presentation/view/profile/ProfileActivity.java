package com.example.cpu11398_local.etalk.presentation.view.profile;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityProfileBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.profile.ProfileViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
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

    private ActivityProfileBinding      binding;
    private Disposable                  disposable;
    private Dialog                      dialog;
    private ProfileViewModel.AvatarCopy avatarCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
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
                case Event.PROFILE_ACTIVITY_SHOW_IMAGE_OPTION:
                    avatarCopy = (ProfileViewModel.AvatarCopy)data[0];
                    Tool.createImageOptionDialog(
                            ProfileActivity.this,
                            REQUEST_CAMERA_CODE,
                            REQUEST_GALLERY_CODE
                    ).show();
                    break;
                /*case Event.LOGIN_ACTIVITY_FINISH_OK:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Test" , "123");
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA_CODE) {
                Bitmap bitmapAvatar = (Bitmap)data.getExtras().get("data");
                binding.profileActivityImgAvatar.setImageBitmap(bitmapAvatar);
                avatarCopy.copy(bitmapAvatar);
            }
            else if (requestCode == REQUEST_GALLERY_CODE) {
                try {
                    Bitmap bitmapAvatar = MediaStore
                            .Images
                            .Media
                            .getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    binding.profileActivityImgAvatar.setImageBitmap(bitmapAvatar);
                    avatarCopy.copy(bitmapAvatar);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
