package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatGroupViewModel;
import com.example.cpu11398_local.etalk.databinding.ActivityChatGroupBinding;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.GlideApp;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatGroupActivity extends BaseActivity {

    private final int REQUEST_TAKE_PHOTO_CODE       = 0;
    private final int REQUEST_RECORD_CODE           = 1;
    private final int REQUEST_CHOOSE_PHOTOS_CODE    = 2;
    private final int REQUEST_CHOOSE_VIDEOS_CODE    = 3;

    @Inject
    @Named("ChatGroupViewModel")
    public ViewModel viewModel;

    private ActivityChatGroupBinding binding;
    private Disposable disposable;
    private ViewModelCallback helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.chat_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_group);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ChatGroupViewModel)viewModel);
        binding.chatActivityLstMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.chatActivityLstMessage.addItemDecoration(new ChatGroupDivider(
                (int)getResources().getDimension(R.dimen.divider_chat_space_same),
                (int)getResources().getDimension(R.dimen.divider_chat_space_diff)
        ));
        binding.chatActivityLstMessage.addOnLayoutChangeListener((View.OnLayoutChangeListener) (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                binding.chatActivityLstMessage.postDelayed(
                        (Runnable) () -> binding.chatActivityLstMessage.scrollToPosition(
                                binding.chatActivityLstMessage.getAdapter().getItemCount() - 1
                        ),
                        100
                );
            }
        });
        binding.chatActivityTxtFriendName.setText(getIntent().getExtras().getString("name"));
        binding.chatActivityTxtFriendStatus.setText(
                getIntent().getExtras().getInt("number")
                        + " "
                        + getString(R.string.app_members)
        );
        addControlKeyboardView(binding.chatActivityLytMessage);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new ChatObserver());
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

    private class ChatObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CHAT_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.CHAT_ACTIVITY_HELPER:
                    if (helper == null) {
                        helper = (ViewModelCallback) data[0];
                        helper.onHelp(Event.create(
                                Event.CHAT_ACTIVITY_VALUE,
                                getIntent().getExtras().getString("user"),
                                getIntent().getExtras().getString("key")
                        ));
                    }
                    break;
                case Event.CHAT_ACTIVITY_GOTO_LAST:
                    binding.chatActivityLstMessage.scrollToPosition(
                            binding.chatActivityLstMessage.getAdapter().getItemCount() - 1
                    );
                    break;
                case Event.CHAT_ACTIVITY_GET_MEDIA:
                    Tool.createMediaOptionDialog(
                            ChatGroupActivity.this,
                            REQUEST_TAKE_PHOTO_CODE,
                            REQUEST_RECORD_CODE,
                            REQUEST_CHOOSE_PHOTOS_CODE,
                            REQUEST_CHOOSE_VIDEOS_CODE
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
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO_CODE:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_IMAGE_URI,
                            Tool.uri
                    ));
                    break;
                case REQUEST_RECORD_CODE:
                    break;
                case REQUEST_CHOOSE_PHOTOS_CODE:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_IMAGE_URI,
                            data.getData()
                    ));
                    break;
                case REQUEST_CHOOSE_VIDEOS_CODE:
                    break;
            }
        }
    }
}
