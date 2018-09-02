package com.example.cpu11398_local.etalk.presentation.view_model.friend;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import com.example.cpu11398_local.etalk.utils.Optional;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class AddFriendViewModel extends     BaseObservable
                                implements  ViewModel,
                                            NetworkChangeReceiver.NetworkChangeListener {
    /**
     * Binding data between {@code hintEvent} and attribute {@code hintWithEvent} of
     * {@code EditText} input hint on view.
     */
    private Event hintEvent = Event.create(Event.NONE);

    @Bindable
    public Event getHintEvent() {
        return hintEvent;
    }

    public void setHintEvent(@NonNull Event hintEvent) {
        this.hintEvent = hintEvent;
        notifyPropertyChanged(BR.hintEvent);
    }

    /**
     * Binding data between {@code phone} and Phone number {@code EditText} on view.
     */
    private String phone = "";

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
        notifyPropertyChanged(BR.searchEnable);
        setResultUser(null);
    }

    /**
     * Binding data between {@code avatarUrl} and attribute {@code url_from_url} of
     * {@code AvatarImageView}.
     */
    private String avatarUrl = null;

    @Bindable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(@Nullable String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    /**
     * Binding data between {@code name} and Fullname {@code EditText} on view.
     */
    private String name = "";

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    /**
     * Need for check to avoid self add.
     */
    private User currentUser = null;

    /**
     * Need for check to avoid add friend again.
     */
    private ArrayList<String> currentFriends = new ArrayList<>();

    /**
     * User found via phone number.
     */
    private User resultUser = null;

    public void setResultUser(@Nullable User resultUser) {
        this.resultUser = resultUser;
        if (resultUser != null) {
            setAvatarUrl(resultUser.getAvatar());
            setName(resultUser.getName());
            setAddedVisibility(currentFriends.contains(resultUser.getUsername()));
            notifyPropertyChanged(BR.addVisibility);
        }
        notifyPropertyChanged(BR.resultVisibility);
        notifyPropertyChanged(BR.searchEnable);
    }

    /**
     * Binding data between {@code networkAvailable} and {@code TextView} for inform
     * state of network.
     */
    private boolean networkAvailable = false;

    @Bindable
    public int getNetworkStateNotificationVisibility() {
        return networkAvailable ? View.GONE : View.VISIBLE;
    }

    public void setNetworkState(@NonNull boolean networkstate) {
        this.networkAvailable = networkstate;
        notifyPropertyChanged(BR.networkStateNotificationVisibility);
        notifyPropertyChanged(BR.searchEnable);
        notifyPropertyChanged(BR.addEnable);
    }

    /**
     * Binding data between {@code added} and {@code Button} for inform
     * state of add friend request.
     */
    private boolean added = false;

    @Bindable
    public int getAddedVisibility() {
        return added ? View.VISIBLE : View.GONE;
    }

    public void setAddedVisibility(@NonNull boolean added) {
        this.added = added;
        notifyPropertyChanged(BR.addedVisibility);
        notifyPropertyChanged(BR.addVisibility);
    }

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * Get current user logging in.
     */
    private Usecase getUserInfoUsecase;

    /**
     * Get current friends.
     */
    private Usecase loadFriendUsecase;

    /**
     * ViewModel use {@code findFriendUsecase} to find user when user want to add new friend.
     */
    private Usecase findFriendUsecase;

    /**
     * ViewModel use {@code addFriendUsecase} to add new friend when user request.
     */
    private Usecase addFriendUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * create new {@code ContentViewModel} with a context.
     */
    @Inject
    public AddFriendViewModel(Context context,
                              @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                              @Named("LoadFriendConversationUsecase") Usecase loadFriendUsecase,
                              @Named("FindFriendUsecase") Usecase findFriendUsecase,
                              @Named("AddFriendUsecase") Usecase addFriendUsecase,
                              NetworkChangeReceiver receiver) {
        this.context            = context;
        this.getUserInfoUsecase = getUserInfoUsecase;
        this.loadFriendUsecase  = loadFriendUsecase;
        this.findFriendUsecase  = findFriendUsecase;
        this.addFriendUsecase   = addFriendUsecase;
        this.receiver           = receiver;
        this.receiver.initReceiver(this.context, this);
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        getUserInfoUsecase.execute(new GetUserInfoObserver(), false);
    }

    /**
     * Call when user click button search.
     * @param view
     */
    public void onSearchRequest(View view) {
        publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_SHOW_LOADING));
        setHintEvent(Event.create(Event.NONE));
        setAddedVisibility(false);
        findFriendUsecase.execute(
                new FindFriendObserver(),
                phone,
                "phone"
        );
    }

    @Bindable
    public boolean getSearchEnable() {
        return networkAvailable && !phone.isEmpty() && resultUser == null;
    }

    /**
     * Call when user click button add.
     * @param view
     */
    public void onAddRequest(View view) {
        if (!added) {
            publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_SHOW_LOADING));
            addFriendUsecase.execute(
                    new AddFriendObserver(),
                    currentUser,
                    resultUser
            );
        }
    }

    @Bindable
    public boolean getAddEnable() {
        return networkAvailable;
    }

    @Bindable
    public int getAddVisibility() {
        return !added
                && currentUser != null
                && resultUser != null
                && !currentUser.getUsername().equals(resultUser.getUsername())
                ? View.VISIBLE
                : View.GONE;
    }

    @Bindable
    public int getResultVisibility() {
        return resultUser == null ? View.GONE : View.VISIBLE;
    }

    /**
     * Called when user click back arrow on Tool bar.
     * @param view
     */
    public void onBackPressed(View view) {
        publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_BACK));
    }

    /**
     * Called when network state change and reassign {@code networkAvailable}
     * according to {@code networkState}.
     * @param networkState current network state.
     */
    @Override
    public void onNetworkChange(boolean networkState) {
        setNetworkState(networkState);
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        getUserInfoUsecase.endTask();
        loadFriendUsecase.endTask();
        findFriendUsecase.endTask();
        addFriendUsecase.endTask();
    }

    /**
     * {@code getUserInfoObserver} is subscribed to usecase to listen event from it.
     */
    private class GetUserInfoObserver extends DisposableSingleObserver<User> {
        @Override
        public void onSuccess(User user) {
            currentUser = user;
            loadFriendUsecase.execute(
                    new LoadFriendObserver(),
                    user.getUsername()
            );
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code LoadFriendObserver} is subscribed to usecase to listen event from it.
     */
    private class LoadFriendObserver extends DisposableObserver<Conversation> {
        @Override
        public void onNext(Conversation conversation) {
            for (String key : conversation.getMembers().keySet()) {
                if (!key.equals(currentUser.getUsername())) {
                    if (!currentFriends.contains(key)) {
                        currentFriends.add(key);
                    }
                    break;
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * {@code FindFriendObserver} is subscribed to usecase to listen event from it.
     */
    private class FindFriendObserver extends DisposableSingleObserver<Optional<User>> {

        private Handler handler = new Handler();

        public FindFriendObserver() {
            handler.postDelayed(
                    () -> {
                        publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
                        setHintEvent(Event.create(Event.ADD_FRIEND_ACTIVITY_TIMEOUT));
                        findFriendUsecase.endTask();
                    },
                    1000 * 10
            );
        }

        @Override
        public void onSuccess(Optional<User> user) {
            handler.removeCallbacksAndMessages(null);
            if (user.isPresent()) {
                publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
                setResultUser(user.get());
            } else {
                publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
                publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_NOT_FOUND));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code AddFriendObserver} is subscribed to usecase to listen event from it.
     */
    private class AddFriendObserver extends DisposableSingleObserver<Boolean> {

        private Handler handler = new Handler();

        public AddFriendObserver() {
            handler.postDelayed(
                    () -> {
                        publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
                        setHintEvent(Event.create(Event.ADD_FRIEND_ACTIVITY_TIMEOUT));
                        addFriendUsecase.endTask();
                    },
                    1000 * 10
            );
        }

        @Override
        public void onSuccess(Boolean isSuccess) {
            handler.removeCallbacksAndMessages(null);
            if (isSuccess) {
                publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
                setAddedVisibility(true);
            } else {
                publisher.onNext(Event.create(Event.ADD_FRIEND_ACTIVITY_HIDE_LOADING));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }
}
