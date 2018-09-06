package com.example.cpu11398_local.etalk.presentation.view_model.group;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.group.FriendAdapter;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import com.example.cpu11398_local.etalk.utils.Optional;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class CreateGroupViewModel extends    BaseObservable
                                  implements ViewModel,
                                             NetworkChangeReceiver.NetworkChangeListener {

    /**
     * Binding data between {@code avatar} and attribute {@code url_from_object} of
     * {@code AvatarImageView}.
     */
    private Bitmap avatar = null;

    @Bindable
    public Bitmap getAvatar() {
        return avatar != null ? avatar : getBitmap(R.drawable.img_group_holder);
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = context.getDrawable(drawableRes);
        Canvas canvas     = new Canvas();
        Bitmap bitmap     = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        canvas.setBitmap(bitmap);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight()
        );
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Binding data between {@code name} and group name {@code EditText} on view.
     */
    private String name = "";

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.createEnable);
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

    public void setNetworkState(boolean networkstate) {
        this.networkAvailable = networkstate;
        notifyPropertyChanged(BR.networkStateNotificationVisibility);
        notifyPropertyChanged(BR.createEnable);
    }

    /**
     * Current user to know who create new group.
     */
    private User currentUser;

    /**
     * Container contain current friend and available for adding to new group.
     */
    private List<User> friends = new ArrayList<>();

    /**
     * Container contain friends selected.
     */
    private List<String> selected = new ArrayList<>();

    /**
     * Binding {@code adapter} and {@code RecyclerView} for friends on view.
     */
    private FriendAdapter adapter = new FriendAdapter(friends, new SelectedCallback() {
        @Override
        public void select(String username) {
            selected.add(username);
            notifyPropertyChanged(BR.createEnable);
        }

        @Override
        public boolean isSelected(String username) {
            return selected.contains(username);
        }

        @Override
        public void remove(String username) {
            selected.remove(username);
            notifyPropertyChanged(BR.createEnable);
        }
    });

    @Bindable
    public FriendAdapter getAdapter() {
        return adapter;
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
     * ViewModel use {@code getUserInfoUsecase} to load user info.
     */
    private Usecase getUserInfoUsecase;

    /**
     * ViewModel use {@code loadFriendConversationUsecase} to load friend conversation info.
     */
    private Usecase loadFriendConversationUsecase;

    /**
     * ViewModel use {@code findFriendUsecase} to load friend info.
     */
    private Usecase findFriendUsecase;

    /**
     * ViewModel use {@code createGroupUsecase} to create a new group.
     */
    private Usecase createGroupUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * create new {@code CreateGroupViewModel} with a context, an usecase to get user info and
     * a network change receiver.
     */
    @Inject
    public CreateGroupViewModel(Context context,
                                @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                                @Named("LoadFriendConversationUsecase") Usecase loadFriendConversationUsecase,
                                @Named("FindFriendUsecase") Usecase findFriendUsecase,
                                @Named("CreateGroupUsecase") Usecase createGroupUsecase,
                                NetworkChangeReceiver receiver) {
        this.context                        = context;
        this.getUserInfoUsecase             = getUserInfoUsecase;
        this.loadFriendConversationUsecase  = loadFriendConversationUsecase;
        this.findFriendUsecase              = findFriendUsecase;
        this.createGroupUsecase             = createGroupUsecase;
        this.receiver                       = receiver;
        this.receiver.initReceiver(this.context, this);
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        if (currentUser == null) {
            getUserInfoUsecase.execute(new GetUserInfoObserver(), false);
        }
    }


    /**
     * Called when user click back arrow on Tool bar.
     * @param view
     */
    public void onBackPressed(View view) {
        publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_BACK));
    }

    /**
     * Called when user click CREATE button on Tool bar.
     * @param view
     */
    public void onCreateGroup(View view) {
        publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_SHOW_LOADING));
        createGroupUsecase.execute(
                new CreateGroupObserver(),
                avatar,
                name,
                currentUser,
                selected
        );
    }

    @Bindable
    public boolean getCreateEnable() {
        return networkAvailable
                && !name.isEmpty()
                && selected.size() >= 2;
    }

    /**
     * Called when user click avatar to change current one.
     * @param view
     */
    public void onChangeAvatar(View view) {
        publisher.onNext(Event.create(
                Event.CREATE_GROUP_ACTIVITY_SHOW_IMAGE_OPTION,
                (AvatarCopy) this::setAvatar
        ));
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
        loadFriendConversationUsecase.endTask();
        findFriendUsecase.endTask();
        createGroupUsecase.endTask();
    }

    /**
     * {@code getUserInfoObserver} is subscribed to usecase to listen event from it.
     */
    private class GetUserInfoObserver extends DisposableSingleObserver<User> {

        @Override
        public void onSuccess(User user) {
            currentUser = user;
            loadFriendConversationUsecase.execute(
                    new LoadFriendConversationObserver(),
                    user.getUsername()
            );
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code LoadFriendConversationObserver} is subscribed to usecase to listen event from it.
     */
    private class LoadFriendConversationObserver extends DisposableObserver<Conversation> {
        @Override
        public void onNext(Conversation conversation) {
            for (String key : conversation.getMembers().keySet()) {
                if (!key.equals(currentUser.getUsername())) {
                    if (!loadedFriend(key)) {
                        findFriendUsecase.execute(
                                new FindFriendObserver(),
                                key,
                                "username"
                        );
                    }
                    break;
                }
            }
        }

        private boolean loadedFriend(String username) {
            for (User user : friends) {
                if (user.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
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

        @Override
        public void onSuccess(Optional<User> user) {
            if (user.isPresent()) {
                friends.add(user.get());
                adapter.notifyItemInserted(friends.size() - 1);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code CreateGroupObserver} is subscribed to usecase to listen event from it.
     */
    private class CreateGroupObserver extends DisposableSingleObserver<Boolean> {

        private Handler handler = new Handler();

        public CreateGroupObserver() {
            handler.postDelayed(
                    () -> {
                        publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_HIDE_LOADING));
                        publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_TIMEOUT));
                        createGroupUsecase.endTask();
                    },
                    avatar == null ? 1000 * 10 : 1000 * 30
            );
        }

        @Override
        public void onSuccess(Boolean isSuccess) {
            handler.removeCallbacksAndMessages(null);
            if (isSuccess) {
                publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_CREATE_OK));
            } else {
                publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_HIDE_LOADING));
                publisher.onNext(Event.create(Event.CREATE_GROUP_ACTIVITY_CREATE_FAILED));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * A callback to update {@link #avatar} when user change avatar that
     * get from camera or gallery.
     */
    public interface AvatarCopy {
        void copy(Bitmap bitmap);
    }

    /**
     * A callback used to update selected friend when user click them on {@code RecyclerView}.
     */
    public interface SelectedCallback {
        void select(String username);
        boolean isSelected(String username);
        void remove(String username);
    }
}
