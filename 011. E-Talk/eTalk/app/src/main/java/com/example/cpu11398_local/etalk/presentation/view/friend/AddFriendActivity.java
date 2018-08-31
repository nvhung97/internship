package com.example.cpu11398_local.etalk.presentation.view.friend;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityAddFriendBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.friend.AddFriendViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddFriendActivity extends BaseActivity {

    @Inject
    @Named("AddFriendViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDataBinding() {
        ActivityAddFriendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((AddFriendViewModel)viewModel);
        addControlKeyboardView(binding.addFriendActivityEdtPhone);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new AddFriendObserver());
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

    private void onShowNotification() {
        Tool.createNotificationDialog(
                this,
                getString(R.string.add_friend_activity_notification_title),
                getString(R.string.add_friend_activity_notification_content),
                getString(R.string.add_friend_activity_notification_positive),
                null,
                getString(R.string.add_friend_activity_notification_negative),
                null
        ).show();
    }

    private class AddFriendObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.ADD_FRIEND_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.ADD_FRIEND_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;
                case Event.ADD_FRIEND_ACTIVITY_NOT_FOUND:
                    onShowNotification();
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
