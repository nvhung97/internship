package com.example.cpu11398_local.etalk.presentation.view_model.profile;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
     * Binding data between {@code avatarUrl} and attribute {@code url_from_url} of
     * {@code AvatarImageView}.
     */
    private String avatarUrl = null;

    @Bindable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    /**
     * Binding data between {@code fullname} and Fullname {@code EditText} on view.
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
     * Binding data between {@code phoneNumber} and Phone number {@code EditText} on view.
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
     * When user request logout, viewModel use {@code logoutUsecase} to perform the action.
     */
    private Usecase getUserInfoUsecase;

    /**
     * Listen network state to inform user check connection again.
     */
    private NetworkChangeReceiver receiver;

    /**
     * User before update.
     */
    private User currentUser;

    /**
     * User after update.
     */
    private User newUser;

    /**
     * A container contain bitmap avatar from camara or gallery.
     */
    private Bitmap bitmapAvatar = null;

    public void setBitmapAvatar(Bitmap bitmapAvatar) {
        this.bitmapAvatar = bitmapAvatar;
        notifyPropertyChanged(BR.updateEnable);
    }

    /**
     * create new {@code ProfileViewModel} with a context, an usecase to get user info and
     * a network change receiver.
     */
    @Inject
    public ProfileViewModel(Context context,
                            @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                            NetworkChangeReceiver receiver) {
        this.context            = context;
        this.getUserInfoUsecase = getUserInfoUsecase;
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
     * Called when user click avatar to change current one.
     * @param view
     */
    public void onChangeAvatar(View view) {
        publisher.onNext(Event.create(
                Event.PROFILE_ACTIVITY_SHOW_IMAGE_OPTION,
                (AvatarCopy) bitmap -> {
                    bitmapAvatar = bitmap;
                    notifyPropertyChanged(BR.updateEnable);
                }
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
     * Call when user click button update.
     * @param view
     */
    public void onUpdateRequest(View view) {

    }

    @Bindable
    public boolean getUpdateEnable() {
        if (bitmapAvatar == null
                && (name.isEmpty() || name.equals(currentUser.getName()))
                && (passwordIcon == R.drawable.ic_edit || password.isEmpty())
                && (phone.isEmpty() || phone.equals(currentUser.getPhone()))) {
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
     * A callback to update {@link #bitmapAvatar} when user change avatar that
     * get from camera or gallery.
     */
    public interface AvatarCopy {
        void copy(Bitmap bitmap);
    }
}
