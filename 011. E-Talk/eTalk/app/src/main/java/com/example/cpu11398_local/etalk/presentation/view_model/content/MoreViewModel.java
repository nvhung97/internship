package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class MoreViewModel extends BaseObservable implements ViewModel{

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
     * Binding data between {@code name} and attribute {@code text} of
     * {@code TextView}.
     */
    private String name = "";

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
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
     * Listener from viewModel parent.
     */
    private ViewModelCallback viewModelCallback;

    /**
     * ViewModel use {@code getUserInfoUsecase} to load user info.
     */
    private Usecase getUserInfoUsecase;

    /**
     * Create new {@code MoreViewModel} with a {@code Context}, a {@code ViewModelCallback} and
     * an usecase for loading user info.
     */
    @Inject
    public MoreViewModel(Context context,
                         ViewModelCallback viewModelCallback,
                         @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase) {
        this.context            = context;
        this.viewModelCallback  = viewModelCallback;
        this.getUserInfoUsecase = getUserInfoUsecase;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        getUserInfoUsecase.execute(new GetUserInfoUsecaseObserver(),true);
    }

    /**
     * Called when user press my profile button.
     * @param view
     */
    public void onMyProfile(View view) {
        publisher.onNext(Event.create(Event.MORE_FRAGMENT_MY_PRORILE));
    }

    /**
     * Called when user press people nearby button.
     * @param view
     */
    public void onPeopleNearby(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press chat rooms button.
     * @param view
     */
    public void onChatRooms(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press Shop button.
     * @param view
     */
    public void onShop(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press game button.
     * @param view
     */
    public void onGame(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press channel button.
     * @param view
     */
    public void onChannel(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press taxi button.
     * @param view
     */
    public void onTaxi(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user press logout button.
     * @param view
     */
    public void onLogout(View view) {
        viewModelCallback.onNewEvent(Event.create(Event.MORE_FRAGMENT_LOGOUT));
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        getUserInfoUsecase.endTask();
    }

    /**
     * {@code GetUserInfoUsecaseObserver} is subscribed to usecase to listen event from it.
     */
    private class GetUserInfoUsecaseObserver extends DisposableObserver<User> {
        @Override
        public void onNext(User user) {
            setName(user.getName());
            setAvatarUrl(user.getAvatar());
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }

        @Override
        public void onComplete() {}
    }
}