package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
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
import io.reactivex.schedulers.Schedulers;

public class ChatPersonUsecase implements Usecase {

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

    private MessagesHolder holder = new MessagesHolder();

    @Override
    public void execute(Object observer, Object... params) {
        switch ((String)params[2]) {
            case SEND:
                executeSendMessage((Message)params[0]);
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
            runFriendHandler(emitter);
            runMessageHandler(emitter);
        });
    }

    private void runFriendHandler(ObservableEmitter<Event> emitter) {
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
                            if (friend != null) {
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
                        })
        );
    }

    @SuppressLint("CheckResult")
    private void loadFriend() {
        for (String key : conversation.getMembers().keySet()) {
            if (!key.equals(username)) {
                disposable.add(
                        userRepository
                                .loadNetworlChangeableUser(key)
                                .subscribe(userOptional -> {
                                    if (friend == null) {
                                        friend = userOptional.get();
                                        loadMessages();
                                    } else {
                                        friend = userOptional.get();
                                    }
                                    needUpdateFriend = true;
                                })
                );
                break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void loadMessages() {
        disposable.add(
                conversationRepository
                        .loadNetworkMessages(conversationKey, username)
                        .subscribe(message -> {
                            holder.addNewMessage(message);
                            needUpdateMessage = true;
                        })
        );
    }

    @SuppressLint("CheckResult")
    private void executeSendMessage(Message message){
        message.setSender(username);
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        conversationRepository
                .pushNetworkMessage(conversationKey, message)
                .subscribeOn(Schedulers.from(executor))
                .observeOn(scheduler)
                .subscribe(result -> {
                    if (result == true) {
                        holder.sendSuccessMessage(message);
                        needUpdateMessage = true;
                    }
                });
    }

    @Override
    public void endTask() {
        friendHandler.removeCallbacksAndMessages(null);
        messageHandler.removeCallbacksAndMessages(null);
        conversationRepository.putLocalMessagesHolder(
                conversationKey,
                holder
        );
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }

    public class MessagesHolder {

        Map<String, Message>    rawMessages     = new HashMap<>();
        Map<String, Message>    sendingMessage  = new HashMap<>();
        List<MessagePersonItem> messages        = new ArrayList<>();

        public MessagesHolder() {}

        public MessagesHolder(Map<String, Message> rawMessages,
                              Map<String, Message> sendingMessage,
                              List<MessagePersonItem> messages) {
            this.rawMessages    = rawMessages;
            this.sendingMessage = sendingMessage;
            this.messages       = messages;
        }

        public void addNewMessage(Message message) {
            if (!rawMessages.containsKey(message.getKey())) {
                rawMessages.put(
                        message.getKey(),
                        message
                );
                MessagePersonItem newItem = new MessagePersonItem(
                        message,
                        decodeData(message.getData(), message.getType()),
                        friend.getAvatar(),
                        decodeTime(message.getTime())
                );
                if (message.getSender().equals(username)) {
                    newItem.setMe(true);
                    newItem.setAvatarVisible(
                            conversation.getMembers().get(friend.getUsername()) > message.getTime()
                                    ? View.VISIBLE
                                    : View.GONE
                    );
                    newItem.setTimeVisible(View.VISIBLE);
                } else {
                    newItem.setMe(false);
                    newItem.setAvatarVisible(View.VISIBLE);
                    newItem.setTimeVisible(View.VISIBLE);
                }
                messages.add(newItem);
                if (messages.size() > 1) {
                    MessagePersonItem lastItem = messages.get(messages.size() - 2).clone();
                    messages.set(messages.size() - 2, lastItem);
                    if (!newItem.isMe() && !lastItem.isMe()) {
                        newItem.setAvatarVisible(View.GONE);
                        lastItem.setTimeVisible(View.GONE);
                    }
                    if (newItem.isMe()) {
                        if (lastItem.isMe()) {
                            lastItem.setTimeVisible(View.GONE);
                        }
                        if (newItem.getAvatarVisible() == View.VISIBLE) {
                            for (int i = messages.size() - 2; i >= 0; --i) {
                                if (messages.get(i).isMe() && messages.get(i).getAvatarVisible() == View.VISIBLE) {
                                    messages.set(i, messages.get(i).clone());
                                    messages.get(i).setAvatarVisible(View.GONE);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (sendingMessage.containsKey(message.getKey())) {
                sendingMessage.get(message.getKey()).setServerTime(message.getTime());
            }
        }

        public void sendNewMessage(Message message) {
            sendingMessage.put(message.getKey(), message);
            rawMessages.put(message.getKey(), message);
            MessagePersonItem newItem = new MessagePersonItem(
                    message,
                    true,
                    decodeData(message.getData(), message.getType()),
                    SENDING_URL,
                    View.VISIBLE,
                    decodeTime(Long.parseLong(message.getKey().substring(username.length()))),
                    View.VISIBLE
            );
            messages.add(newItem);
            if (messages.size() > 1 && messages.get(messages.size() - 2).isMe()) {
                messages.set(messages.size() - 2, messages.get(messages.size() - 2).clone());
                messages.get(messages.size() - 2 ).setTimeVisible(View.GONE);
            }
        }

        public void sendSuccessMessage(Message message) {
            sendingMessage.remove(message.getKey());
            for (int i = messages.size() - 1; i >=0 ; --i) {
                if (messages.get(i).getMessage().getKey().equals(message.getKey())) {
                    messages.set(i, messages.get(i).clone());
                    messages.get(i).getMessage().setServerTime(Long.parseLong(message.getKey().substring(username.length())));
                    messages.get(i).setAvatar(SENT_URL);
                    break;
                }
            }
        }

        public void newConversationInfo(Conversation conversation) {
            long friendTime = conversation.getMembers().get(friend.getUsername());
            for (int i = messages.size() - 1; i >= 0; --i) {
                if (messages.get(i).isMe() && friendTime >= messages.get(i).getMessage().getTime()) {
                    if (messages.get(i).getAvatarVisible() == View.GONE) {
                        messages.set(i, messages.get(i).clone());
                        messages.get(i).setAvatarVisible(View.VISIBLE);
                        for (int j = i - 1; j >= 0; --j) {
                            if (messages.get(j).isMe() && messages.get(j).getAvatarVisible() == View.VISIBLE) {
                                messages.set(j, messages.get(j).clone());
                                messages.get(j).setAvatarVisible(View.GONE);
                                break;
                            }
                        }
                    } else if (messages.get(i).getAvatar().equals(SENT_URL)) {
                        messages.set(i, messages.get(i).clone());
                        messages.get(i).setAvatar(friend.getAvatar());
                        for (int j = i - 1; j >= 0; --j) {
                            if (messages.get(j).isMe() && messages.get(j).getAvatarVisible() == View.VISIBLE) {
                                messages.set(j, messages.get(j).clone());
                                messages.get(j).setAvatarVisible(View.GONE);
                                if (messages.get(j).getAvatar().equals(friend.getAvatar())) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        public void setRawMessages(Map<String, Message> rawMessages) {
            this.rawMessages = rawMessages;
        }

        public void setSendingMessage(Map<String, Message> sendingMessage) {
            this.sendingMessage = sendingMessage;
        }

        public void setMessages(List<MessagePersonItem> messages) {
            this.messages = messages;
        }

        public List<MessagePersonItem> getMessages() {
            return new ArrayList<>(messages);
        }

        public Map<String, Message> getRawMessages() {
            return rawMessages;
        }

        public Map<String, Message> getSendingMessage() {
            return sendingMessage;
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
