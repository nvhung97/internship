package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class ContentViewModel implements ViewModel,
                                         PopupMenu.OnMenuItemClickListener,
                                         ViewPager.OnPageChangeListener,
                                         NetworkChangeReceiver.NetworkChangeListener {

    /**
     * Determine which tab is selected to change layout.
     * Value reassigned in {@link #onTabClick(View)}.
     */
    public ObservableInt currentTab = new ObservableInt(0);

    /**
     * Binding data between {@code isNetworkAvailable} and {@code TextView} for inform
     * state of network.
     */
    public ObservableBoolean isNetworkAvailable = new ObservableBoolean(false);

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * When user request logout, viewModel use {@code logoutUsecase} to perform the action.
     */
    private Usecase logoutUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * create new {@code ContentViewModel} with a context.
     */
    @Inject
    public ContentViewModel(Context context,
                            @Named("LogoutUsecase") Usecase logoutUsecase,
                            NetworkChangeReceiver receiver) {
        this.context        = context;
        this.logoutUsecase  = logoutUsecase;
        this.receiver       = receiver;
        receiver.initReceiver(this.context, this);
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
    }

    /**
     * Called when user click one tab. Reassign value of {@code currentTab}.
     * corresponding to {@code view} as tab is clicked.
     * @param view
     */
    public void onTabClick(View view) {
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

    /**
     * Called when user click camera button on tool bar.
     * @param view
     */
    public void onCameraClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click add friend button on tool bar.
     * @param view
     */
    public void onAddFriendClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click add image button on tool bar.
     * @param view
     */
    public void onAddImageClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click bell button on tool bar.
     * @param view
     */
    public void onBellClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click QR code button on tool bar.
     * @param view
     */
    public void onQRCodeClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click setting button on tool bar.
     * @param view
     */
    public void onSettingClick(View view) {
        publisher.onNext(
                Event.create(
                        Event.CONTENT_ACTIVITY_SHOW_POPUP_SETTING,
                        view, this
                )
        );
    }

    /**
     * Called when user click plus button.
     * @param view
     */
    public void onPlusClick(View view) {
        publisher.onNext(
                Event.create(
                        Event.CONTENT_ACTIVITY_SHOW_POPUP_MENU,
                        view, this
                )
        );
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
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_add_friend:
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_scan_qr_code:
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_etalk_for_desktop:
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_etalk_calendar:
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_activity_menu_logout:
                onLogoutRequest();
                break;
        }
        return true;
    }

    /**
     * Call when user request logout. See {@link #onMenuItemClick(MenuItem)}
     */
    public void onLogoutRequest() {
        publisher.onNext(Event.create(Event.CONTENT_ACTIVITY_SHOW_LOADING));
        new LogoutObserver();
    }

    /**
     * Called when network state change and reassign {@code isNetworkAvailable}
     * according to {@code networkState}.
     * @param networkState current network state.
     */
    @Override
    public void onNetworkChange(boolean networkState) {
        isNetworkAvailable.set(networkState);
    }

    /**
     * {@code LogoutObserver} is subscribed to usecase to listen event from it.
     */
    private class LogoutObserver extends DisposableSingleObserver<Boolean> {

        private Handler handler = new Handler();

        public LogoutObserver() {
            handler.postDelayed(
                    () -> {
                        publisher.onNext(Event.create(Event.CONTENT_ACTIVITY_HIDE_LOADING));
                        logoutUsecase.endTask();
                    },
                    1000 * 10
            );
        }

        @Override
        public void onSuccess(Boolean isSuccess) {
            handler.removeCallbacksAndMessages(null);
            if (isSuccess) {
                publisher.onNext(Event.create(Event.CONTENT_ACTIVITY_LOGOUT));
            } else {
                publisher.onNext(Event.create(Event.LOGIN_ACTIVITY_HIDE_LOADING));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * Catch event {@code onPageSelected} on {@code ViewPager} and
     * reassign {@code currentTab}.
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        currentTab.set(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void endTask() {

    }
}
