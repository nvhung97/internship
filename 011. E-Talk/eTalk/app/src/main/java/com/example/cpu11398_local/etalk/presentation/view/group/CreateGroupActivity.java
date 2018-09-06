package com.example.cpu11398_local.etalk.presentation.view.group;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityCreateGroupBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.chat.ChatActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.group.CreateGroupViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CreateGroupActivity extends BaseActivity {

    private final int REQUEST_CAMERA_CODE  = 0;
    private final int REQUEST_GALLERY_CODE = 1;

    @Inject
    @Named("CreateGroupViewModel")
    public ViewModel    viewModel;

    private Disposable                      disposable;
    private Dialog                          dialog;
    private CreateGroupViewModel.AvatarCopy avatarCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.create_group_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        ActivityCreateGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((CreateGroupViewModel)viewModel);
        binding.createGroupActivityRvMember.setLayoutManager(new LinearLayoutManager(this));
        binding.createGroupActivityRvMember.setHasFixedSize(true);
        addControlKeyboardView(
                binding.createGroupActivityEdtName
        );
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new CreateGroupObserver());
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

    private class CreateGroupObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CREATE_GROUP_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_SHOW_IMAGE_OPTION:
                    avatarCopy = (CreateGroupViewModel.AvatarCopy)data[0];
                    Tool.createImageOptionDialog(
                            CreateGroupActivity.this,
                            REQUEST_CAMERA_CODE,
                            REQUEST_GALLERY_CODE
                    ).show();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_CREATE_OK:
                    startActivity(
                            new Intent(
                                    CreateGroupActivity.this,
                                    ChatActivity.class
                            )
                    );
                    finish();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_CREATE_FAILED:
                    Toast.makeText(
                            CreateGroupActivity.this,
                            getString(R.string.create_group_activity_create_failed),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case Event.CREATE_GROUP_ACTIVITY_TIMEOUT:
                    Toast.makeText(
                            CreateGroupActivity.this,
                            getString(R.string.app_request_timeout),
                            Toast.LENGTH_SHORT
                    ).show();
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
                Bitmap bitmapAvatar = (Bitmap)data.getExtras().get("data");
                avatarCopy.copy(bitmapAvatar);
            }
            else if (requestCode == REQUEST_GALLERY_CODE) {
                Bitmap bitmapAvatar = Tool.getImageWithUri(this, data.getData());
                avatarCopy.copy(bitmapAvatar);
            }
        }
    }
}
