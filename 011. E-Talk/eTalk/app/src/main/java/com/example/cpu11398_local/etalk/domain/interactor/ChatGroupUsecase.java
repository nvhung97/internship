package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import com.example.cpu11398_local.etalk.utils.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChatGroupUsecase implements Usecase {

    private final String SENT_URL       = "https://firebasestorage.googleapis.com/v0/b/etalkchat.appspot.com/o/sent.png?alt=media&token=4e7cf2d2-22d5-47d8-9e5a-12bdbbcaedb1";
    private final String SENDING_URL    = "https://firebasestorage.googleapis.com/v0/b/etalkchat.appspot.com/o/sending.png?alt=media&token=32ca8ddf-6b82-42e8-85d9-2e5d726dfe97";
    private final String FIRST_LOAD     = "first_load";
    private final String LOAD_MORE      = "load_more";
    private final String SEND           = "send";

    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;
    private ConversationRepository  conversationRepository;

    private Handler messageHandler      = new Handler();
    private boolean needUpdateMessage   = false;

    @Inject
    public ChatGroupUsecase(Executor executor,
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

    private Conversation        conversation;
    private Map<String, User>   friends = new HashMap<>();

    private MessagesGroupHolder holder = new MessagesGroupHolder();

    @Override
    public void execute(Object observer, Object... params) {
        switch ((String)params[2]) {
            case SEND:
                //executeSendMessage((Message)params[0]);
                break;
            case FIRST_LOAD:
                username        = (String)params[0];
                conversationKey = (String)params[1];
                executeFirstLoadCase((DisposableObserver<Event>)observer);
                break;
            case LOAD_MORE:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void executeFirstLoadCase(DisposableObserver<Event> observer) {
        disposable.add(
                buildFirstLoadObservable()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(observer)
        );
    }

    private Observable<Event> buildFirstLoadObservable() {
        return Observable.create(emitter -> {
            loadConversation();
            runMessageHandler(emitter);
        });
    }

    private void runMessageHandler(ObservableEmitter<Event> emitter) {
        messageHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (needUpdateMessage) {
                            needUpdateMessage = false;
                            emitter.onNext(Event.create(
                                    Event.CHAT_ACTIVITY_MESSAGES,
                                    holder.getMessages()
                            ));
                        }
                        messageHandler.postDelayed(this, 500);
                    }
                },
                500
        );
    }

    @SuppressLint("CheckResult")
    private void loadConversation() {
        /*disposable.add(
                conversationRepository
                        .loadLocalConversation(conversationKey)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<Conversation>() {
                            @Override
                            public void onSuccess(Conversation item) {
                                conversation = item;
                                loadFriend();
                                onError(null);
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.add(
                                        conversationRepository
                                                .loadNetworkConversation(conversationKey)
                                                .subscribe(item -> {
                                                    Conversation oldConversation = conversation;
                                                    if (conversation == null) {
                                                        conversation = item;
                                                        loadFriend();
                                                    } else {
                                                        conversation = item;
                                                    }
                                                    if (friend != null && oldConversation != null) {
                                                        if (oldConversation.getMembers().get(friend.getUsername()) < conversation.getMembers().get(friend.getUsername())) {
                                                            new Handler().postDelayed(
                                                                    () -> {
                                                                        holder.newConversationInfo(conversation);
                                                                        needUpdateMessage = true;
                                                                    },
                                                                    500
                                                            );
                                                        }
                                                    }
                                                    conversationRepository.insertLocalConversation(conversation);
                                                })
                                );
                            }
                        })
        );*/
    }

    @Override
    public void endTask() {
        messageHandler.removeCallbacksAndMessages(null);
        /*conversationRepository.putLocalMessagesPersonHolder(
                conversationKey,
                holder
        );*/
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }

    public class MessagesGroupHolder{

        Map<String, Message>    rawMessages     = new HashMap<>();
        Map<String, Message>    sendingMessage  = new HashMap<>();
        List<MessageGroupItem>  messages        = new ArrayList<>();

        public MessagesGroupHolder() {}

        public MessagesGroupHolder(Map<String, Message> rawMessages,
                                   Map<String, Message> sendingMessage,
                                   List<MessageGroupItem> messages) {
            this.rawMessages = rawMessages;
            this.sendingMessage = sendingMessage;
            this.messages = messages;
        }

        public void addNewMessage(Message message) {
            //TODO
        }

        public void sendNewMessage(Message message) {
            //TODO
        }

        public void sendSuccessMessage(Message message) {
            //TODO
        }

        public void newConversationInfo(Conversation conversation) {
            //TODO
        }

        public Map<String, Message> getRawMessages() {
            return rawMessages;
        }

        public void setRawMessages(Map<String, Message> rawMessages) {
            this.rawMessages = rawMessages;
        }

        public Map<String, Message> getSendingMessage() {
            return sendingMessage;
        }

        public void setSendingMessage(Map<String, Message> sendingMessage) {
            this.sendingMessage = sendingMessage;
        }

        public List<MessageGroupItem> getMessages() {
            return new ArrayList<>(messages);
        }

        public void setMessages(List<MessageGroupItem> messages) {
            this.messages = messages;
        }

        private Object decodeData(String data, long type) {
            if (type == Message.TEXT) {
                return data;
            }
            return null;
        }

        private String decodeTime(long time) {
            Calendar when = Calendar.getInstance();
            when.setTimeInMillis(time);
            int hour = when.get(Calendar.HOUR_OF_DAY);
            int minute = when.get(Calendar.MINUTE);
            String minuteZero = minute < 10 ? "0" : "";
            return hour + ":" + minuteZero + minute;
        }
    }
}
