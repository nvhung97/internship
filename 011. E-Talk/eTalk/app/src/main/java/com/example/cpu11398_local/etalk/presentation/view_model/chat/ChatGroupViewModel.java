package com.example.cpu11398_local.etalk.presentation.view_model.chat;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupAdapter;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class ChatGroupViewModel  extends BaseObservable implements ViewModel, ViewModelCallback {

    /**
     * Container contain messages.
     */
    private List<MessageGroupItem> messages = new ArrayList<>();

    /**
     * Binding {@code adapter} and {@code RecyclerView} for contacts on view.
     */
    private MessageGroupAdapter adapter = new MessageGroupAdapter(messages);

    @Bindable
    public MessageGroupAdapter getAdapter(){
        return adapter;
    }

    /**
     * Binding data between {@code textMessage} and chat {@code EditText} on view.
     */
    private String textMessage = "";

    @Bindable
    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
        notifyPropertyChanged(BR.textMessage);
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
     * Used to load message and send new message.
     */
    private Usecase chatGroupUsecase;

    /**
     * create new {@code ChatGroupViewModel} with a context.
     */
    @Inject
    public ChatGroupViewModel(Context context,
                              @Named("ChatGroupUsecase") Usecase chatGroupUsecase) {
        this.context            = context;
        this.chatGroupUsecase   = chatGroupUsecase;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        publisher.onNext(Event.create(Event.CHAT_ACTIVITY_HELPER, this));
    }

    /**
     * Called when user click back arrow on Tool bar.
     * @param view
     */
    public void onBackPressed(View view) {
        publisher.onNext(Event.create(Event.CHAT_ACTIVITY_BACK));
    }

    /**
     * Called when user click voice call button on Tool bar.
     * @param view
     */
    public void onVoiceCallClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click video call button on Tool bar.
     * @param view
     */
    public void onVideoCallClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click more button on Tool bar.
     * @param view
     */
    public void onMoreClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click emoticon button on Message bar.
     * @param view
     */
    public void onEmoticonClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click attach button on Message bar.
     * @param view
     */
    public void onAttachClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click record button on Message bar.
     * @param view
     */
    public void onRecordClick(View view) {
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click image button on Message bar.
     * @param view
     */
    public void onImageClick(View view) {
        publisher.onNext(Event.create(Event.CHAT_ACTIVITY_GET_MEDIA));
    }

    /**
     * Called when user click send button to send a Message.
     * After send current messgase, reset {@code textMessage} to empty.
     * @param view
     */
    public void onSendMessage(View view) {
        chatGroupUsecase.execute(
                null,
                new Message(
                        username,
                        textMessage,
                        Message.TEXT
                ),
                null,
                "send_text"
        );
        setTextMessage("");
    }

    private String username;
    private String conversationKey;

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        chatGroupUsecase.endTask();
    }

    @Override
    public void onChildViewModelSubscribeObserver(Observer<Event> observer, int code) {

    }

    @Override
    public void onHelp(Event event) {
        Object[] data = event.getData();
        switch (event.getType()) {
            case Event.CHAT_ACTIVITY_VALUE:
                username = (String)data[0];
                conversationKey = (String)data[1];
                executeFirstLoad();
                break;
            case Event.CHAT_ACTIVITY_SEND_IMAGE_URI:
                executeSendImageUri((Uri)data[0]);
                break;
        }
    }

    private void executeFirstLoad() {
        chatGroupUsecase.execute(
                new ChatObserver(),
                username,
                conversationKey,
                "first_load"
        );
    }

    private void executeSendImageUri(Uri uri) {
        chatGroupUsecase.execute(
                null,
                uri,
                null,
                "send_image_uri"
        );
    }

    /**
     * {@code ChatObserver} is subscribed to usecase to listen event from it.
     */
    private class ChatObserver extends DisposableObserver<Event> {

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CHAT_ACTIVITY_MESSAGES:
                    adapter.onNewData(
                            (List<MessageGroupItem>) data[0],
                            () -> {
                                publisher.onNext(Event.create(Event.CHAT_ACTIVITY_GOTO_LAST));
                                return null;
                            });
                    break;
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
}
