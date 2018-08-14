package com.example.cpu11398_local.etalk.presentation.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityContentBinding;
import com.example.cpu11398_local.etalk.presentation.view_model.ContentViewModel;
import com.example.cpu11398_local.etalk.utils.Tool;

public class ContentActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener{

    public ContentViewModel contentViewModel;

    private ActivityContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content);
        contentViewModel = (ContentViewModel) getLastCustomNonConfigurationInstance();
        if (contentViewModel == null) {
            //ContentActivity.getAppComponent(this).inject(this);
            contentViewModel = new ContentViewModel();
        }
        binding.setContentViewModel(contentViewModel);
    }

    @Override
    void onSubscribeViewModel() {

    }

    @Override
    void onUnSubscribeViewModel() {

    }

    @Override
    Object onSaveViewModel() {
        return contentViewModel;
    }

    @Override
    void onEndTaskViewModel() {
        contentViewModel.endTask();
    }

    /**
     * Display popup menu contain some features when user click MORE button.
     * @param view
     */
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.inflate(R.menu.menu_plus);
        popupMenu.setOnMenuItemClickListener(this);
        Tool.forcePopupMenuShowIcon(popupMenu);
        popupMenu.show();
    }

    /**
     * Catch event and do action corresponding to item clicked.
     * @param item item clicked.
     * @return {@code true} mean the listener handler this click event.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.content_activity_menu_create_group:
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_add_friend:
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_scan_qr_code:
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_etalk_for_desktop:
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_etalk_calendar:
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
