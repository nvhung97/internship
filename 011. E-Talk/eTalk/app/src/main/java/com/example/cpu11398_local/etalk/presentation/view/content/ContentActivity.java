package com.example.cpu11398_local.etalk.presentation.view.content;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityContentBinding;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.ContentPagerAdapter;
import com.example.cpu11398_local.etalk.presentation.view.friend.AddFriendActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.content.ContentViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ContentActivity extends BaseActivity {

    @Inject
    @Named("ContentViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.content_activity_status_bar)
        );
    }

    @Override
    public void onDataBinding() {
        ActivityContentBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_content);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ContentViewModel)viewModel);
        binding.setPagerAdapter(new ContentPagerAdapter(getSupportFragmentManager()));
        addControlKeyboardView(binding.contentActivityEdtSearch);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new ContentObserver());
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

    public void onShowPopupMenu(View view, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.inflate(R.menu.menu_plus);
        popupMenu.setOnMenuItemClickListener(listener);
        Tool.forcePopupMenuShowIcon(popupMenu);
        popupMenu.show();
    }

    /*private void onShowLoading() {
        dialog = Tool.createProcessingDialog(this);
        dialog.show();
    }

    private void onHideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }*/

    private void onLogout() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    private class ContentObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CONTENT_ACTIVITY_SHOW_POPUP_MENU:
                    onShowPopupMenu(
                            (View)data[0],
                            (PopupMenu.OnMenuItemClickListener)data[1]
                    );
                    break;
                case Event.CONTENT_ACTIVITY_MENU_ADD_FRIEND:
                    startActivity(new Intent(ContentActivity.this, AddFriendActivity.class));
                    break;
                /*case Event.CONTENT_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
                    break;*/
                case Event.CONTENT_ACTIVITY_LOGOUT:
                    onLogout();
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
