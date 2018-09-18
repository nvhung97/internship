package com.example.cpu11398_local.etalk.domain.interactor;

import android.os.Handler;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Event;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ChatPersonUsecase implements Usecase {

    private final String FIRST_LOAD = "first_load";
    private final String LOAD_MORE  = "load_more";
    private final String SEND       = "send";

    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;
    private ConversationRepository  conversationRepository;

    private Handler                 friendHandler       = new Handler();
    private boolean                 needUpdateFriend    = false;
    private Handler                 messageHandler      = new Handler();
    private boolean                 needUpdateMessage   = false;

    @Inject
    public ChatPersonUsecase(Executor executor,
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

    private String username;
    private String conversationKey;

    private Conversation conversation;
    private User         friend;

    @Override
    public void execute(Object observer, Object... params) {
        username        = (String)params[0];
        conversationKey = (String)params[1];
        switch ((String)params[2]) {
            case SEND:
                break;
            case FIRST_LOAD:
                disposable.add(
                        buildFirstLoadObservable(username, conversationKey)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribeWith((DisposableObserver<Event>)observer)
                );
                break;
            case LOAD_MORE:
                break;
        }
    }

    private Observable<Event> buildFirstLoadObservable(String username, String conversationKey) {
        return Observable.create(emitter -> {
            friendHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (needUpdateFriend && friend != null) {
                                needUpdateFriend = false;
                                emitter.onNext(Event.create(
                                        Event.CHAT_ACTIVITY_FRIEND,
                                        friend
                                ));
                            }
                            friendHandler.postDelayed(this, 5000);
                        }
                    },
                    5000
            );
            messageHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (needUpdateMessage/* && friend != null*/) {
                                needUpdateMessage = false;
                                /*emitter.onNext(Event.create(
                                        Event.CHAT_ACTIVITY_FRIEND,
                                        friend
                                ));*/
                            }
                            messageHandler.postDelayed(this, 500);
                        }
                    },
                    500
            );
            conversationRepository
                    .loadNetworkConversation(conversationKey)
                    .subscribe(item -> {
                        if (conversation == null) {
                            conversation = item;
                            for (String key : conversation.getMembers().keySet()) {
                                if (!key.equals(username)) {
                                    userRepository
                                            .loadNetworlChangeableUser(key)
                                            .subscribe(userOptional -> {
                                                friend = userOptional.get();
                                                needUpdateFriend = true;
                                            });
                                    break;
                                }
                            }
                        } else {
                            conversation = item;
                        }
                    });
        });
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}
