package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class ContentViewModel implements ViewModel,
                                         ViewModelCallback,
                                         PopupMenu.OnMenuItemClickListener,
                                         ViewPager.OnPageChangeListener,
                                         NetworkChangeReceiver.NetworkChangeListener {
    /**
     * Data used to emit to child view Model
     */
    private User                currentUser     = null;
    private List<Conversation>  conversations   = new ArrayList<>();
    private List<User>          friends         = new ArrayList<>();

    /**
     * Determine which tab is selected to change layout.
     * Value reassigned in {@link #onTabClick(View)}.
     */
    public ObservableInt currentTab = new ObservableInt(0);

    /**
     * Binding data between {@code networkAvailable} and {@code TextView} for inform
     * state of network.
     */
    public ObservableBoolean isNetworkAvailable = new ObservableBoolean(false);

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> viewPublisher = PublishSubject.create();

    /**
     * Publisher will emit event to child viewModel. thay listen these event via a observer.
     */
    private PublishSubject<Event> messagesPublisher = PublishSubject.create();
    private PublishSubject<Event> contactsPublisher = PublishSubject.create();
    private PublishSubject<Event> groupsPublisher = PublishSubject.create();
    private PublishSubject<Event> timelinePublisher = PublishSubject.create();
    private PublishSubject<Event> morePublisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * viewModel use {@code loadContentDataUsecase} to load all data.
     */
    private Usecase loadContentDataUsecase;

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
                            @Named("LoadContentDataUsecase") Usecase loadContentDataUsecase,
                            @Named("LogoutUsecase") Usecase logoutUsecase,
                            NetworkChangeReceiver receiver) {
        this.context                = context;
        this.loadContentDataUsecase = loadContentDataUsecase;
        this.logoutUsecase          = logoutUsecase;
        this.receiver               = receiver;
        this.receiver.initReceiver(this.context, this);
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        viewPublisher.subscribe(observer);
        if (currentUser == null) {
            loadContentDataUsecase.execute(new LoadContentDataObserver());
        }
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
        viewPublisher.onNext(Event.create(Event.CONTENT_ACTIVITY_MENU_ADD_FRIEND));
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
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click plus button.
     * @param view
     */
    public void onPlusClick(View view) {
        viewPublisher.onNext(
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
                viewPublisher.onNext(Event.create(Event.CONTENT_ACTIVITY_MENU_CREATE_GROUP));
                break;
            case R.id.content_activity_menu_add_friend:
                viewPublisher.onNext(Event.create(Event.CONTENT_ACTIVITY_MENU_ADD_FRIEND));
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
        }
        return true;
    }

    /**
     * Call when user request logout. See {@link #onHelp(Event)}
     */
    public void onLogoutRequest() {
        logoutUsecase.execute(null, null);
        viewPublisher.onNext(Event.create(Event.CONTENT_ACTIVITY_LOGOUT));
    }

    /**
     * Called when network state change and reassign {@code networkAvailable}
     * according to {@code networkState}.
     * @param networkState current network state.
     */
    @Override
    public void onNetworkChange(boolean networkState) {
        isNetworkAvailable.set(networkState);
        messagesPublisher.onNext(Event.create(
                Event.CONTENT_ACTIVITY_EMIT_NETWORK_STATUS,
                networkState
        ));
    }

    /**
     * Called when child viewModel subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void onChildViewModelSubscribeObserver(Observer<Event> observer, int code) {
        switch (code) {
            case ViewModelCallback.MESSAGES:
                messagesPublisher.subscribe(observer);
                break;
            case ViewModelCallback.CONTACTS:
                contactsPublisher.subscribe(observer);
                break;
            case ViewModelCallback.GROUPS:
                groupsPublisher.subscribe(observer);
                break;
            case ViewModelCallback.TIMELINE:
                timelinePublisher.subscribe(observer);
                break;
            case ViewModelCallback.MORE:
                morePublisher.subscribe(observer);
                break;
        }
    }

    /**
     * Called when viewModel child emit an event.
     * @param event event that emitted to viewModel Parent.
     */
    @Override
    public void onHelp(Event event) {
        Object[] data = event.getData();
        switch (event.getType()) {
            case Event.MORE_FRAGMENT_LOGOUT:
                onLogoutRequest();
                break;
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

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        logoutUsecase.endTask();
        loadContentDataUsecase.endTask();
    }

    /**
     * {@code LoadContentDataObserver} is subscribed to usecase to listen event from it.
     */
    private class LoadContentDataObserver extends DisposableObserver<Event> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CONTENT_ACTIVITY_EMIT_USER:
                    currentUser = (User)data[0];
                    messagesPublisher.onNext(Event.create(
                            Event.CONTENT_ACTIVITY_EMIT_USER,
                            currentUser
                    ));
                    contactsPublisher.onNext(Event.create(
                            Event.CONTENT_ACTIVITY_EMIT_USER,
                            currentUser
                    ));
                    groupsPublisher.onNext(Event.create(
                            Event.CONTENT_ACTIVITY_EMIT_USER,
                            currentUser
                    ));
                    morePublisher.onNext(Event.create(
                            Event.CONTENT_ACTIVITY_EMIT_USER,
                            currentUser
                    ));
                    break;
                case Event.CONTENT_ACTIVITY_EMIT_CONVERSATIONS:
                    conversations = (List<Conversation>)data[0];
                    messagesPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_CONVERSATIONS,
                                    conversations
                            )
                    );
                    contactsPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_CONVERSATIONS,
                                    getFriendConversations()
                            ));
                    groupsPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_CONVERSATIONS,
                                    getGroupConversations()
                            ));
                    break;
                case Event.CONTENT_ACTIVITY_EMIT_FRIENDS:
                    friends = (List<User>)data[0];
                    messagesPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_FRIENDS,
                                    friends
                            )
                    );
                    contactsPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_FRIENDS,
                                    friends
                            ));
                    groupsPublisher.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_FRIENDS,
                                    friends
                            ));
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private List<Conversation> getFriendConversations() {
            return conversations
                    .stream()
                    .filter(conversation -> conversation.getType() == Conversation.PERSON)
                    .collect(Collectors.toList());
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private List<Conversation> getGroupConversations() {
            return conversations
                    .stream()
                    .filter(conversation -> conversation.getType() == Conversation.GROUP)
                    .collect(Collectors.toList());
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }
}
