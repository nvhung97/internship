package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.os.Build;
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
import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoadContentDataUsecase implements Usecase {

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
                .create(emitter ->
                    loadUser()
                            .subscribe(user -> {
                                emitter.onNext(
                                        Event.create(
                                                Event.CONTENT_ACTIVITY_EMIT_USER,
                                                user
                                        )
                                );
                                if (currentUser == null) {
                                    currentUser = user;
                                    loadConversation(emitter);
                                } else {
                                    currentUser = user;
                                }
                            })
                );
    }

    private Observable<User> loadUser() {
        return userRepository.getCacheChangeableUser();
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadConversation(ObservableEmitter<Event> emitter) {
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
                    loadFriends(conversation, emitter);
                    emitter.onNext(
                            Event.create(
                                    Event.CONTENT_ACTIVITY_EMIT_CONVERSATIONS,
                                    new ArrayList<>(conversations.values())
                            )
                    );

                });
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadFriends(Conversation conversation, ObservableEmitter<Event> emitter) {
        conversation.getMembers().forEach((key, time) -> {
            if (!friends.containsKey(key) && !key.equals(currentUser.getUsername())) {
                friends.put(
                        key,
                        new User("", "", "", "", null, 0L)
                );
                userRepository
                        .loadNetworlChangeableUser(key)
                        .subscribe(friend -> {
                            if (friend.isPresent()) {
                                friends.replace(key, friend.get());
                                emitter.onNext(
                                        Event.create(
                                                Event.CONTENT_ACTIVITY_EMIT_FRIENDS,
                                                new ArrayList<>(friends.values())
                                        )
                                );
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