package com.example.cpu11398_local.etalk.presentation.view.chat.person;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityPersonChatBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatPersonViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatPersonActivity extends BaseActivity {

    @Inject
    @Named("ChatPersonViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
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
        ActivityPersonChatBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_person_chat);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ChatPersonViewModel)viewModel);
        binding.chatActivityLstMessage.setLayoutManager(new LinearLayoutManager(this));
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
                                getIntent().getExtras().getString("key"),
                                getIntent().getExtras().getString("name"),
                                getIntent().getExtras().getLong("number")
                        ));
                    }
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
