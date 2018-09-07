package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class GroupViewModel extends BaseObservable /*implements ViewModel */{

    /**
     * Current user to get friend from conversation.
     *//*
    private User currentUser;

    *//**
     * Container contain all friends in groups.
     *//*
    private Map<String, User> friends = new HashMap<>();

    *//**
     * Container contain group conversations.
     *//*
    private List<Conversation> conversations = new ArrayList<>();

    *//**
     * Container contain group conversations key.
     *//*
    private List<String> conversationKey = new ArrayList<>();

    *//**
     * Binding {@code adapter} and {@code RecyclerView} for contacts on view.
     *//*
    private GroupAdapter adapter = new GroupAdapter(conversations, friends, new ActionCallback() {
        @Override
        public void chatWith(Conversation conversation) {
            publisher.onNext(Event.create(Event.CONTACT_FRAGMENT_CHAT));
        }
    });

    @Bindable
    public GroupAdapter getAdapter() {
        return adapter;
    }

    *//**
     * Publisher will emit event to view. View listen these event via a observer.
     *//*
    private PublishSubject<Event> publisher = PublishSubject.create();

    *//**
     * Context is used to get resource or toast something on screen.
     *//*
    private Context context;

    *//**
     * Listener from viewModel parent.
     *//*
    private ViewModelCallback viewModelCallback;

    *//**
     * ViewModel use {@code getUserInfoUsecase} to load user info.
     *//*
    private Usecase getUserInfoUsecase;

    *//**
     * ViewModel use {@code loadFriendConversationUsecase} to load group conversation info.
     *//*
    private Usecase loadGroupConversationUsecase;

    *//**
     * ViewModel use {@code findFriendUsecase} to load friend info.
     *//*
    private Usecase findFriendUsecase;

    *//**
     * Create new {@code ContactViewModel} with a {@code Context}, a {@code ViewModelCallback} and
     * an usecase for loading user info.
     *//*
    @Inject
    public GroupViewModel(Context context,
                          ViewModelCallback viewModelCallback,
                          @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                          @Named("LoadGroupConversationUsecase") Usecase loadGroupConversationUsecase,
                          @Named("FindFriendUsecase") Usecase findFriendUsecase) {
        this.context                        = context;
        this.viewModelCallback              = viewModelCallback;
        this.getUserInfoUsecase             = getUserInfoUsecase;
        this.loadGroupConversationUsecase   = loadGroupConversationUsecase;
        this.findFriendUsecase              = findFriendUsecase;
    }

    *//**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     *//*
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        if (currentUser == null) {
            getUserInfoUsecase.execute(new GetUserInfoObserver(), false);
        }
    }

    *//**
     * Called when this viewModel destroyed to inform usecase stop current task.
     *//*
    @Override
    public void endTask() {
        getUserInfoUsecase.endTask();
        loadGroupConversationUsecase.endTask();
        findFriendUsecase.endTask();
    }

    *//**
     * {@code getUserInfoObserver} is subscribed to usecase to listen event from it.
     *//*
    private class GetUserInfoObserver extends DisposableSingleObserver<User> {

        @Override
        public void onSuccess(User user) {
            currentUser = user;
            loadGroupConversationUsecase.execute(
                    new LoadGroupConversationObserver(),
                    user.getUsername()
            );
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    *//**
     * {@code LoadGroupConversationObserver} is subscribed to usecase to listen event from it.
     *//*
    private class LoadGroupConversationObserver extends DisposableObserver<Conversation> {
        @Override
        public void onNext(Conversation conversation) {
            for (String key : conversation.getMembers().keySet()) {
                if (key.equals(currentUser.getUsername())) continue;
                if (!key.equals(currentUser.getUsername())) {
                    if (!conversationKey.contains(key)) {
                        findFriendUsecase.execute(
                                new FindFriendObserver(conversation),
                                key,
                                "username"
                        );
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

    *//**
     * {@code FindFriendObserver} is subscribed to usecase to listen event from it.
     *//*
    private class FindFriendObserver extends DisposableSingleObserver<Optional<User>> {

        private Conversation conversation;

        public FindFriendObserver(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void onSuccess(Optional<User> user) {
            if (user.isPresent()) {
                friends.add(user.get());
                conversationMap.put(user.get().getUsername(), conversation);
                adapter.notifyItemInserted(friends.size() - 1);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    *//**
     * A callback to get action when user click item on {@code RecyclerView}.
     *//*
    public interface ActionCallback {
        void chatWith(Conversation conversation);
    }*/
}
