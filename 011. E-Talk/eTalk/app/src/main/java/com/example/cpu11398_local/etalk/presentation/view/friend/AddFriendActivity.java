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

    private class AddFriendObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                /*case Event.CONTENT_ACTIVITY_SHOW_POPUP_MENU:
                    onShowPopupMenu(
                            (View)data[0],
                            (PopupMenu.OnMenuItemClickListener)data[1]
                    );
                    break;
                case Event.CONTENT_ACTIVITY_MENU_ADD_FRIEND:
                    startActivity(new Intent(ContentActivity.this, AddFriendActivity.class));
                    break;
                *//*case Event.CONTENT_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;*//*
                case Event.CONTENT_ACTIVITY_LOGOUT:
                    onLogout();
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
