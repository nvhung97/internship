package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import com.example.cpu11398_local.etalk.utils.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private final String SEND_TEXT      = "send_text";
    private final String SEND_IMAGE_URI = "send_image_uri";

    private Context                 context;
    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;
    private ConversationRepository  conversationRepository;

    private Handler messageHandler      = new Handler();
    private boolean needUpdateMessage   = false;

    @Inject
    public ChatGroupUsecase(Context context,
                            Executor executor,
                            Scheduler scheduler,
                            CompositeDisposable disposable,
                            UserRepository userRepository,
                            ConversationRepository conversationRepository) {
        this.context                = context;
        this.executor               = executor;
        this.scheduler              = scheduler;
        this.disposable             = disposable;
        this.userRepository         = userRepository;
        this.conversationRepository = conversationRepository;
    }

    private String username;
    private String conversationKey;

    private Conversation oldConversation, newConversation;
    private Map<String, User>   friends = new HashMap<>();

    private MessagesGroupHolder holder = new MessagesGroupHolder();

    @Override
    public void execute(Object observer, Object... params) {
        switch ((String)params[2]) {
            case SEND_TEXT:
                executeSendMessage((Message)params[0]);
                break;
            case SEND_IMAGE_URI:
                executeSendImageUri((Uri)params[0]);
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
            runMessageHandler(emitter);
            loadConversation();
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
        disposable.add(
                conversationRepository
                        .loadLocalConversation(conversationKey)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<Conversation>() {
                            @Override
                            public void onSuccess(Conversation item) {
                                newConversation = item;
                                loadFriend();
                                onError(null);
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.add(
                                        conversationRepository
                                                .loadNetworkConversation(conversationKey)
                                                .subscribe(item -> {
                                                    oldConversation = newConversation;
                                                    newConversation = item;
                                                    if (oldConversation == null) {
                                                        loadFriend();
                                                    }
                                                    if (friends.size() == newConversation.getMembers().size() - 1) {
                                                        for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                                                            if (!entry.getKey().equals(username)) {
                                                                if (entry.getValue() != oldConversation.getMembers().get(entry.getKey())) {
                                                                    new Handler().postDelayed(
                                                                            () -> {
                                                                                holder.newConversationInfo(oldConversation, newConversation);
                                                                                needUpdateMessage = true;
                                                                            },
                                                                            500
                                                                    );
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    conversationRepository.insertLocalConversation(newConversation);
                                                })
                                );
                            }
                        })
        );
    }

    private void loadFriend() {
        for (String key : newConversation.getMembers().keySet()) {
            if (!key.equals(username)) {
                disposable.add(
                        userRepository
                                .loadLocalUser(key)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribeWith(new DisposableSingleObserver<User>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onSuccess(User user) {
                                        friends.put(user.getUsername(), user);
                                        if (friends.size() == newConversation.getMembers().size() - 1) {
                                            loadMessages();
                                        }
                                        onError(null);
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onError(Throwable e) {
                                        disposable.add(
                                                userRepository
                                                        .loadNetworlChangeableUser(key)
                                                        .subscribe(userOptional -> {
                                                            if (friends.containsKey(userOptional.get().getUsername())) {
                                                                friends.replace(
                                                                        userOptional.get().getUsername(),
                                                                        userOptional.get()
                                                                );
                                                            } else {
                                                                friends.put(
                                                                        userOptional.get().getUsername(),
                                                                        userOptional.get()
                                                                );
                                                                if (friends.size() == newConversation.getMembers().size() - 1) {
                                                                    loadMessages();
                                                                }
                                                            }
                                                        })
                                        );
                                    }
                                })
                );
            }
        }
    }

    private void loadMessages() {
        disposable.add(
                conversationRepository
                        .loadLocalMessagesGroupHolder(this, conversationKey)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<MessagesGroupHolder>(){
                            @Override
                            public void onSuccess(MessagesGroupHolder messagesGroupHolder) {
                                holder = messagesGroupHolder;
                                needUpdateMessage = true;
                                for (Message message : holder.getSendingMessage().values()) {
                                    disposable.add(
                                            conversationRepository
                                                    .pushNetworkMessage(conversationKey, message)
                                                    .subscribeOn(Schedulers.from(executor))
                                                    .observeOn(scheduler)
                                                    .subscribe(result -> {
                                                        if (result == true) {
                                                            holder.sendSuccessMessage(message);
                                                            needUpdateMessage = true;
                                                        }
                                                    })
                                    );
                                }
                                onError(null);
                            }
                            @Override
                            public void onError(Throwable e) {
                                disposable.add(
                                        conversationRepository
                                                .loadNetworkMessages(conversationKey, username)
                                                .subscribe(message -> {
                                                    holder.addNewMessage(message);
                                                    needUpdateMessage = true;
                                                })
                                );
                            }
                        })
        );
    }

    private void executeSendMessage(Message message) {
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        disposable.add(
                conversationRepository
                        .pushNetworkMessage(conversationKey, message)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(result -> {
                            if (result == true) {
                                holder.sendSuccessMessage(message);
                                needUpdateMessage = true;
                            }
                        })
        );
    }

    private void executeSendImageUri(Uri uri) {
        Message message = new Message(
                username,
                null,
                Message.IMAGE
        );
        File file = new File(
                context.getFilesDir(),
                message.getKey() + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri))
        );
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            OutputStream os = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while((len = is.read(buff)) > 0){
                os.write(buff,0, len);
            }
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setData(file.getAbsolutePath());
        holder.sendNewMessage(message);
        needUpdateMessage = true;
    }

    @Override
    public void endTask() {
        messageHandler.removeCallbacksAndMessages(null);
        conversationRepository.putLocalMessagesGroupHolder(
                conversationKey,
                holder
        );
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
                MessageGroupItem newItem = new MessageGroupItem(message)
                        .newData(decodeData(message.getData(), message.getType()))
                        .newTime(decodeTime(message.getTime()))
                        .newTimeVisible(View.VISIBLE);
                if (message.getSender().equals(username)) {
                    newItem = newItem
                            .newMe(true)
                            .newAvatar(SENT_URL)
                            .newAvatarVisible(View.VISIBLE);
                    Map<String, String> seens = newItem.getSeen();
                    for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                        if (!entry.getKey().equals(username)) {
                            if (entry.getValue() > message.getTime()) {
                                seens.put(
                                        entry.getKey(),
                                        friends.get(entry.getKey()).getAvatar()
                                );
                                for (int i = messages.size() - 1; i >= 0; --i) {
                                    if (messages.get(i).getSeen().containsKey(entry.getKey())){
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.remove(entry.getKey());
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!seens.isEmpty()) {
                        newItem = newItem
                                .newSeen(seens)
                                .newAvatarVisible(View.GONE);
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (messages.get(i).isMe() && !sendingMessage.containsKey(messages.get(i).getMessage().getKey())) {
                                if (messages.get(i).getAvatarVisible() == View.GONE) break;
                                messages.set(
                                        i,
                                        messages.get(i).newAvatarVisible(View.GONE)
                                );
                            }
                        }
                    }
                    if (!messages.isEmpty() && messages.get(messages.size() - 1).isMe()) {
                        messages.set(
                                messages.size() - 1,
                                messages.get(messages.size() - 1).newTimeVisible(View.GONE)
                        );
                    }
                } else {
                    newItem = newItem
                            .newMe(false)
                            .newName(friends.get(message.getSender()).getName())
                            .newNameVisible(View.VISIBLE)
                            .newAvatar(friends.get(message.getSender()).getAvatar())
                            .newAvatarVisible(View.VISIBLE);
                    Map<String, String> seens = newItem.getSeen();
                    for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                        if (!entry.getKey().equals(username) && !entry.getKey().equals(message.getSender())) {
                            if (entry.getValue() > message.getTime()) {
                                seens.put(
                                        entry.getKey(),
                                        friends.get(entry.getKey()).getAvatar()
                                );
                                for (int i = messages.size() - 1; i >= 0; --i) {
                                    if (messages.get(i).getSeen().containsKey(entry.getKey())){
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.remove(entry.getKey());
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!seens.isEmpty()) {
                        newItem = newItem.newSeen(seens);
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (messages.get(i).isMe() && !sendingMessage.containsKey(messages.get(i).getMessage().getKey())) {
                                if (messages.get(i).getAvatarVisible() == View.GONE) break;
                                messages.set(
                                        i,
                                        messages.get(i).newAvatarVisible(View.GONE)
                                );
                            }
                        }
                    }
                    if (!messages.isEmpty() && messages.get(messages.size() - 1).getMessage().getSender().equals(newItem.getMessage().getSender())) {
                        messages.set(
                                messages.size() - 1,
                                messages.get(messages.size() - 1).newTimeVisible(View.GONE)
                        );
                        newItem = newItem
                                .newAvatarVisible(View.GONE)
                                .newNameVisible(View.GONE);
                    }
                }
                messages.add(newItem);
            } else if (sendingMessage.containsKey(message.getKey())) {
                sendingMessage.get(message.getKey()).setServerTime(message.getTime());
            }
        }

        public void sendNewMessage(Message message) {
            sendingMessage.put(message.getKey(), message);
            rawMessages.put(message.getKey(), message);
            MessageGroupItem newItem = new MessageGroupItem(
                    message,
                    true,
                    null,
                    View.GONE,
                    decodeData(message.getData(), message.getType()),
                    SENDING_URL,
                    View.VISIBLE,
                    decodeTime(Long.parseLong(message.getKey().substring(username.length()))),
                    View.VISIBLE
            );
            messages.add(newItem);
            if (messages.size() > 1 && messages.get(messages.size() - 2).isMe()) {
                messages.set(
                        messages.size() - 2,
                        messages.get(messages.size() - 2).newTimeVisible(View.GONE)
                );
            }
        }

        public void sendSuccessMessage(Message message) {
            sendingMessage.remove(message.getKey());
            for (int i = messages.size() - 1; i >= 0; --i) {
                if (messages.get(i).getMessage().getKey().equals(message.getKey())) {
                    messages.set(
                            i,
                            messages.get(i).newAvatar(SENT_URL)
                    );
                    messages.get(i).getMessage().setServerTime(Long.parseLong(message.getKey().substring(username.length())));
                    break;
                }
            }
        }

        public void newConversationInfo(Conversation oldConversation, Conversation newConversation) {
            for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                if (!entry.getKey().equals(username)) {
                    if (entry.getValue() != oldConversation.getMembers().get(entry.getKey())) {
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (!sendingMessage.containsKey(messages.get(i).getMessage().getKey())) {
                                if (entry.getValue() > messages.get(i).getMessage().getTime()
                                        && !entry.getKey().equals(messages.get(i).getMessage().getSender())) {
                                    if (!messages.get(i).getSeen().containsKey(entry.getKey())) {
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.put(
                                                entry.getKey(),
                                                friends.get(entry.getKey()).getAvatar()
                                        );
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        for (int j = i; j >= 0; --j) {
                                            if (messages.get(j).isMe() && !sendingMessage.containsKey(messages.get(j).getMessage().getKey())){
                                                if (messages.get(j).getAvatarVisible() == View.GONE) break;
                                                messages.set(
                                                        j,
                                                        messages.get(j).newAvatarVisible(View.GONE)
                                                );
                                            }
                                        }
                                        for (int j = i - 1; j >= 0; --j) {
                                            if (messages.get(j).getSeen().containsKey(entry.getKey())) {
                                                Map<String, String> _map = messages.get(j).getSeen();
                                                _map.remove(entry.getKey());
                                                messages.set(
                                                        j,
                                                        messages.get(j).newSeen(_map)
                                                );
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
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
            if (type == Message.IMAGE) {
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
