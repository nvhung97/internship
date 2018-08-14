package com.example.cpu11398_local.etalk.presentation.view_model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.LinearLayout;
import com.example.cpu11398_local.etalk.R;
import io.reactivex.Observer;

public class ContentViewModel implements ViewModel<Void>{

    /**
     * Determine which tab is selected to change layout.
     * Value reassigned in {@link #onTabClickListener(View)}.
     */
    public ObservableInt currentTab = new ObservableInt(0);

    public ContentViewModel() {
    }

    /**
     * Called when user click one tab. Reassign value of {@code currentTab}.
     * corresponding to {@code view} as tab is clicked.
     * @param view
     */
    public void onTabClickListener(View view) {
        switch (view.getId()) {
            case R.id.content_activity_tab_messages:
                currentTab.set(0);
                break;
            case R.id.content_activity_tab_contacts:
                currentTab.set(1);
                break;
            case R.id.content_activity_tab_groups:
                currentTab.set(2);
                break;
            case R.id.content_activity_tab_timeline:
                currentTab.set(3);
                break;
            case R.id.content_activity_tab_more:
                currentTab.set(4);
                break;
        }
    }

    @Override
    public void subscribeObserver(Observer<Void> observer) {

    }

    @Override
    public void endTask() {

    }
}
