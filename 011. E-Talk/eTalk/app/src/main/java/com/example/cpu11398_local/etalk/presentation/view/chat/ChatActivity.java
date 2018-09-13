package com.example.cpu11398_local.etalk.presentation.view.chat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityChatBinding;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatActivity extends BaseActivity {

    @Inject
    @Named("ChatViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;

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
        ActivityChatBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ChatViewModel) viewModel);
        binding.chatActivityTxtFriendName.setText(getIntent().getExtras().getString("name"));
        if (getIntent().getExtras().getLong("type") == Conversation.GROUP) {
            binding.chatActivityTxtFriendStatus.setText(
                    getIntent().getExtras().getInt("number")
                    + " "
                    + getString(R.string.app_members)
            );
        } else if (System.currentTimeMillis() - getIntent().getExtras().getLong("number") < 10000) {
            binding.chatActivityTxtFriendStatus.setText(getString(R.string.app_online));
        } else {
            binding.chatActivityTxtFriendStatus.setText(getString(R.string.app_offline));
        }
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
