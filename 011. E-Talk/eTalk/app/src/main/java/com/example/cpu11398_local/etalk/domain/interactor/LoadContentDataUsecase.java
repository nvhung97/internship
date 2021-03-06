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
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
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
                    runHandler(emitter);
                    disposable.add(
                            loadUser()
                                    .subscribe(user -> {
                                        if (currentUser == null) {
                                            currentUser = user;
                                            loadConversation();
                                        } else {
                                            currentUser = user;
                                        }
                                    })
                    );
                });
    }

    private void runHandler(ObservableEmitter<Event> emitter) {
        handler.postDelayed(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        emitter.onNext(Event.create(
                                Event.CONTENT_ACTIVITY_EMIT_DATA,
                                currentUser,
                                conversations.entrySet().stream().sorted((e1, e2) -> {
                                    long time1 = e1.getValue().getLastMessage().getTime();
                                    long time2 = e2.getValue().getLastMessage().getTime();
                                    if (time1 > time2) return -1;
                                    if (time1 < time2) return 1;
                                    return 0;
                                }).map(e -> e.getValue()).collect(Collectors.toList()),
                                new HashMap<>(friends)
                        ));
                        handler.postDelayed(this, 3000);
                    }
                },
                3000
        );
    }

    private Observable<User> loadUser() {
        return userRepository.getCacheChangeableUser();
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadConversation() {
        disposable.add(
                conversationRepository
                        .loadAllLocalConversation()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(
                                conversationList -> {
                                    for (Conversation conversation : conversationList) {
                                        if (conversations.containsKey(conversation.getKey())) {
                                            conversations.replace(
                                                    conversation.getKey(),
                                                    conversation
                                            );
                                        } else {
                                            conversations.put(
                                                    conversation.getKey(),
                                                    conversation
                                            );
                                        }
                                        loadFriends(conversation);
                                    }
                                    disposable.add(
                                            conversationRepository
                                                    .loadNetworkRelationships(currentUser.getUsername())
                                                    .subscribe(conversation -> {
                                                        if (conversations.containsKey(conversation.getKey())) {
                                                            conversations.replace(
                                                                    conversation.getKey(),
                                                                    conversation
                                                            );
                                                        } else {
                                                            conversations.put(
                                                                    conversation.getKey(),
                                                                    conversation
                                                            );
                                                        }
                                                        loadFriends(conversation);
                                                        conversationRepository.insertLocalConversation(conversation);
                                                    })
                                    );
                                },
                                e -> {
                                    disposable.add(
                                            conversationRepository
                                                    .loadNetworkRelationships(currentUser.getUsername())
                                                    .subscribe(conversation -> {
                                                        if (conversations.containsKey(conversation.getKey())) {
                                                            conversations.replace(
                                                                    conversation.getKey(),
                                                                    conversation
                                                            );
                                                        } else {
                                                            conversations.put(
                                                                    conversation.getKey(),
                                                                    conversation
                                                            );
                                                        }
                                                        loadFriends(conversation);
                                                        conversationRepository.insertLocalConversation(conversation);
                                                    })
                                    );
                                }
                        )
        );
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadFriends(Conversation conversation) {
        conversation.getMembers().forEach((key, time) -> {
            if (!friends.containsKey(key) && !key.equals(currentUser.getUsername())) {
                disposable.add(
                        userRepository
                                .loadLocalUser(key)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribe(
                                        user -> {
                                            if (friends.containsKey(key)) {
                                                friends.replace(key, user);
                                            } else {
                                                friends.put(key, user);
                                            }
                                            disposable.add(
                                                    userRepository
                                                            .loadNetworlChangeableUser(key)
                                                            .subscribe(friend -> {
                                                                if (friend.isPresent()) {
                                                                    if (friends.containsKey(key)) {
                                                                        friends.replace(key, friend.get());
                                                                    } else {
                                                                        friends.put(key, friend.get());
                                                                    }
                                                                    if (conversation.getType() == Conversation.PERSON) {
                                                                        userRepository.inserLocalUser(friend.get());
                                                                    }
                                                                }
                                                            })
                                            );
                                        },
                                        e -> {
                                            disposable.add(
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
                                                                if (conversation.getType() == Conversation.PERSON) {
                                                                    userRepository.inserLocalUser(friend.get());
                                                                }
                                                            })
                                            );
                                        }
                                )
                );
            }
        });
    }

    @Override
    public void endTask() {
        handler.removeCallbacksAndMessages(null);
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}