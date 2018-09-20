package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
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
import io.reactivex.SingleObserver;
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

    private MessagesHolder holder = new MessagesHolder();

    @Override
    public void execute(Object observer, Object... params) {
        switch ((String)params[2]) {
            case SEND:
                executeSendMessage((Message)params[0], (SingleObserver<Boolean>)observer);
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
                });
    }

    @SuppressLint("CheckResult")
    private void loadFriend() {
        for (String key : conversation.getMembers().keySet()) {
            if (!key.equals(username)) {
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
                        });
                break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void loadMessages() {
        conversationRepository
                .loadNetworkMessages(conversationKey, username)
                .subscribe(message -> {
                    holder.addNewMessage(message);
                    needUpdateMessage = true;
                });
    }

    @SuppressLint("CheckResult")
    private void executeSendMessage(Message message, SingleObserver<Boolean> observer){
        message.setSender(username);
        Log.e("Test", "Send");
        conversationRepository
                .pushNetworkMessage(conversationKey, message)
                .subscribeOn(Schedulers.from(executor))
                .observeOn(scheduler)
                .subscribeWith(observer);
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }

    public class MessagesHolder {

        Map<String, Message>    rawMessages = new HashMap<>();
        List<MessagePersonItem> messages = new ArrayList<>();

        public void addNewMessage(Message message) {
            if (!rawMessages.containsKey(message.getSender() + message.getTime())) {
                rawMessages.put(
                        message.getSender() + message.getTime(),
                        message
                );
                MessagePersonItem newItem = new MessagePersonItem(
                        message,
                        decodeData(message.getData()),
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
            }
        }

        public void newConversationInfo(Conversation conversation) {
            Log.e("TestSize", " " + messages.size());
            if (friend != null) {
                long friendTime = conversation.getMembers().get(friend.getUsername());
                Log.e("TestFriendTime", " " + friendTime);
                for (int i = messages.size() - 1; i >= 0; --i) {
                    Log.e("TestI", " " + i);
                    if (messages.get(i).isMe() && friendTime >= messages.get(i).getMessage().getTime()) {
                        Log.e("TestUserTime", " " + messages.get(i).getMessage().getTime());
                        if (messages.get(i).getAvatarVisible() == View.GONE) {
                            messages.set(i, messages.get(i).clone());
                            messages.get(i).setAvatarVisible(View.VISIBLE);
                            for (int j = i - 1; j >= 0; --j) {
                                Log.e("TestJ", " " + j);
                                if (messages.get(j).isMe() && messages.get(j).getAvatarVisible() == View.VISIBLE) {
                                    messages.set(j, messages.get(j).clone());
                                    messages.get(j).setAvatarVisible(View.GONE);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

        public List<MessagePersonItem> getMessages() {
            return new ArrayList<>(messages);
        }

        private Object decodeData(String data) {
            switch (data.substring(0,2)) {
                case FirebaseTree.Database.Messages.Keys.Key.Data.TEXT:
                    return data.substring(3);
                /*case FirebaseTree.Database.Messages.Keys.Key.Data.IMAGE:
                    setText(getContext().getString(R.string.app_image));
                    break;
                case FirebaseTree.Database.Messages.Keys.Key.Data.SOUND:
                    setText(getContext().getString(R.string.app_sound));
                    break;
                case FirebaseTree.Database.Messages.Keys.Key.Data.VIDEO:
                    setText(getContext().getString(R.string.app_video));
                    break;
                case FirebaseTree.Database.Messages.Keys.Key.Data.FILE:
                    setText(getContext().getString(R.string.app_sound));
                    break;*/
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
