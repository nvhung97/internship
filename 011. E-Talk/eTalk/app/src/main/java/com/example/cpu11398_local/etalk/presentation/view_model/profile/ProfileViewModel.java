package com.example.cpu11398_local.etalk.presentation.view_model.profile;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;
import com.example.cpu11398_local.etalk.R;

public class ProfileViewModel extends    BaseObservable
                              implements ViewModel,
                                         NetworkChangeReceiver.NetworkChangeListener {

    /**
     * Binding data between {@code avatarUrl} and attribute {@code url_from_object} of
     * {@code AvatarImageView}.
     */
    private String avatarUrl = null;

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatar);
    }

    /**
     * A container contain bitmap avatar from camara or gallery.
     */
    private Bitmap bitmapAvatar = null;

    public void setBitmapAvatar(Bitmap bitmapAvatar) {
        this.bitmapAvatar = bitmapAvatar;
        notifyPropertyChanged(BR.updateEnable);
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public Object getAvatar() {
        return bitmapAvatar != null ? bitmapAvatar : avatarUrl;
    }

    /**
     * Binding data between {@code name} and Fullname {@code EditText} on view.
     */
    private String name = "";

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.updateEnable);
    }

    /**
     * Binding data between {@code username} and Username {@code EditText} on view.
     */
    private String username = "";

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    /**
     * Binding data between {@code password} and Password {@code EditText} on view.
     */
    private String password = "********";

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.updateEnable);
    }

    /**
     * Binding data between {@code passwordIcon} and button control password
     * {@code EditText} on view.
     */
    private int passwordIcon = R.drawable.ic_edit;

    @Bindable
    public Drawable getPasswordIcon(){
        return context.getDrawable(passwordIcon);
    }

    public void setPasswordIcon(int passwordIcon) {
        this.passwordIcon = passwordIcon;
        notifyPropertyChanged(BR.passwordIcon);
    }

    /**
     * Binding data between {@code phone} and Phone number {@code EditText} on view.
     */
    private String phone = "";

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
        notifyPropertyChanged(BR.updateEnable);
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
        notifyPropertyChanged(BR.updateEnable);
    }

    /**
     * User before update. Used to compare to new info if have any change.
     */
    private User currentUser;

    /**
     * User after update.
     */
    private User newUser;

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
     * When user request update, viewModel use {@code updateUserInfoUsecase} to perform the action.
     */
    private Usecase updateUserInfoUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * create new {@code ProfileViewModel} with a context, an usecase to get user info and
     * a network change receiver.
     */
    @Inject
    public ProfileViewModel(Context context,
                            @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                            @Named("UpdateUserInfoUsecase") Usecase updateUserInfoUsecase,
                            NetworkChangeReceiver receiver) {
        this.context                = context;
        this.getUserInfoUsecase     = getUserInfoUsecase;
        this.updateUserInfoUsecase  = updateUserInfoUsecase;
        this.receiver               = receiver;
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
     * Called when user click avatar to change current one.
     * @param view
     */
    public void onChangeAvatar(View view) {
        publisher.onNext(Event.create(
                Event.PROFILE_ACTIVITY_SHOW_IMAGE_OPTION,
                (AvatarCopy) this::setBitmapAvatar
        ));
    }

    /**
     * Called when user click eye button to show or hide password.
     * @param view
     */
    public void onChangeVisiblePasswordState(View view) {
        switch (passwordIcon) {
            case R.drawable.ic_edit:
                setPasswordIcon(R.drawable.ic_show_password);
                setPassword("");
                notifyPropertyChanged(BR.passwordEnable);
                break;
            case R.drawable.ic_show_password:
                setPasswordIcon(R.drawable.ic_hide_password);
                notifyPropertyChanged(BR.passwordInputType);
                break;
            case R.drawable.ic_hide_password:
                setPasswordIcon(R.drawable.ic_show_password);
                notifyPropertyChanged(BR.passwordInputType);
                break;
        }
    }

    @Bindable
    public int getPasswordInputType() {
        switch (passwordIcon) {
            case R.drawable.ic_hide_password:
                return InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
            default:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }
    }

    @Bindable
    public boolean getPasswordEnable() {
        switch (passwordIcon) {
            case R.drawable.ic_edit:
                return false;
            default:
                return true;
        }
    }

    /**
     * Called when user click back arrow on Tool bar.
     * @param view
     */
    public void onBackPressed(View view) {
        publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_BACK));
    }

    /**
     * Call when user click button update.
     * @param view
     */
    public void onUpdateRequest(View view) {
        publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_SHOW_LOADING));
        newUser = new User(
                name,
                username,
                passwordIcon == R.drawable.ic_edit ? currentUser.getPassword() : password,
                phone,
                bitmapAvatar == null ? currentUser.getAvatar() : null
        );
        updateUserInfoUsecase.execute(
                new UpdateUserInfoObserver(bitmapAvatar != null),
                newUser,
                bitmapAvatar
        );
    }

    @Bindable
    public boolean getUpdateEnable() {
        if (!networkAvailable) {
            return false;
        }
        if (name.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            return false;
        }
        if (bitmapAvatar == null
                && name.equals(currentUser.getName())
                && passwordIcon == R.drawable.ic_edit
                && phone.equals(currentUser.getPhone())) {
            return false;
        }
        return true;
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
        updateUserInfoUsecase.endTask();
    }

    /**
     * {@code getUserInfoObserver} is subscribed to usecase to listen event from it.
     */
    private class GetUserInfoObserver extends DisposableSingleObserver<User> {

        @Override
        public void onSuccess(User user) {
            currentUser = user;
            setAvatarUrl(user.getAvatar());
            setName(user.getName());
            setUsername(user.getUsername());
            setPhone(user.getPhone());
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code LoginObserver} is subscribed to usecase to listen event from it.
     */
    private class UpdateUserInfoObserver extends DisposableSingleObserver<Boolean> {

        private Handler handler = new Handler();

        public UpdateUserInfoObserver(boolean hasImage) {
            handler.postDelayed(
                    () -> {
                        publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_HIDE_LOADING));
                        publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_TIME_OUT));
                        updateUserInfoUsecase.endTask();
                    },
                    hasImage ? 1000 * 30 : 1000 * 10
            );
        }

        @Override
        public void onSuccess(Boolean isSuccess) {
            handler.removeCallbacksAndMessages(null);
            if (isSuccess) {
                publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_HIDE_LOADING));
                publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_UPDATE_OK));
                currentUser = newUser;
                newUser = null;
                bitmapAvatar = null;
                setPasswordIcon(R.drawable.ic_edit);
                notifyPropertyChanged(BR.passwordInputType);
                notifyPropertyChanged(BR.passwordEnable);
                notifyPropertyChanged(BR.updateEnable);
            } else {
                publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_HIDE_LOADING));
                publisher.onNext(Event.create(Event.PROFILE_ACTIVITY_UPDATE_FAILED));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * A callback to update {@link #bitmapAvatar} when user change avatar that
     * get from camera or gallery.
     */
    public interface AvatarCopy {
        void copy(Bitmap bitmap);
    }
}
