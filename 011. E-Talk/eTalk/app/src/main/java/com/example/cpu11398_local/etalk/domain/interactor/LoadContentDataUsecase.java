package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoadContentDataUsecase implements Usecase {

    private Handler                 handler;
    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;
    private ConversationRepository  conversationRepository;

    @Inject
    public LoadContentDataUsecase(Executor executor,
                                  Scheduler scheduler,
                                  CompositeDisposable disposable,
                                  UserRepository userRepository,
                                  ConversationRepository conversationRepository) {
        this.handler                = new Handler();
        this.executor               = executor;
        this.scheduler              = scheduler;
        this.disposable             = disposable;
        this.userRepository         = userRepository;
        this.conversationRepository = conversationRepository;
    }

    User                            currentUser     = null;
    HashMap<String, Conversation>   conversations   = new HashMap<>();
    HashMap<String, User>           friends         = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void execute(Object observer, Object... params) {
        disposable.add(
                buildObservable()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith((DisposableObserver<Event>)observer)
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Observable<Event> buildObservable() {
        return Observable
                .create(emitter -> {
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    emitter.onNext(Event.create(
                                            Event.CONTENT_ACTIVITY_EMIT_ALl,
                                            currentUser,
                                            new ArrayList<>(conversations.values()),
                                            new HashMap<>(friends)
                                    ));
                                    handler.postDelayed(this, 5000);
                                }
                            },
                            5000
                    );
                    loadUser()
                            .subscribe(user -> {
                                if (currentUser == null) {
                                    currentUser = user;
                                    loadConversation();
                                } else {
                                    currentUser = user;
                                }
                            });
                });
    }

    private Observable<User> loadUser() {
        return userRepository.getCacheChangeableUser();
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadConversation() {
        conversationRepository
                .loadNetworkRelationships(currentUser.getUsername())
                .subscribe(conversation -> {
                    if (conversations.containsKey(conversation.getCreator() + conversation.getTime())) {
                        conversations.replace(
                                conversation.getCreator() + conversation.getTime(),
                                conversation
                        );
                    } else {
                        conversations.put(
                                conversation.getCreator() + conversation.getTime(),
                                conversation
                        );
                    }
                    loadFriends(conversation);
                });
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadFriends(Conversation conversation) {
        conversation.getMembers().forEach((key, time) -> {
            if (!friends.containsKey(key) && !key.equals(currentUser.getUsername())) {
                userRepository
                        .loadNetworlChangeableUser(key)
                        .subscribe(friend -> {
                            if (friend.isPresent()) {
                                if (friends.containsKey(key)) {
                                    friends.replace(key, friend.get());
                                } else {
                                    friends.put(key, friend.get());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}