package com.example.cpu11398_local.etalk.presentation.view_model.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
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

public class ChatGroupViewModel extends     BaseObservable
                                implements  ViewModel,
                                            ViewModelCallback,
                                            PopupMenu.OnMenuItemClickListener {

    /**
     * Container contain messages.
     */
    private List<MessageGroupItem> messages = new ArrayList<>();

    /**
     * Binding {@code adapter} and {@code RecyclerView} for contacts on view.
     */
    private MessageGroupAdapter adapter = new MessageGroupAdapter(messages, this);

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
    public ChatGroupViewModel(@Named("ChatGroupUsecase") Usecase chatGroupUsecase) {
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
        publisher.onNext(
                Event.create(
                        Event.CHAT_ACTIVITY_SHOW_POPUP_MENU,
                        view, this
                )
        );
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
        publisher.onNext(Event.create(Event.CHAT_ACTIVITY_ATTACH));
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
                textMessage,
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
                context = (Context)data[2];
                executeFirstLoad();
                break;
            case Event.CHAT_ACTIVITY_SEND_IMAGE_URI:
                executeSendImageUri((Uri)data[0]);
                break;
            case Event.CHAT_ACTIVITY_SEND_FILE:
                executeSendFile((Uri)data[0]);
                break;
            case Event.CHAT_ACTIVITY_DOWNLOAD:
                executeDownload((int)data[0]);
                break;
            case Event.CHAT_ACTIVITY_CANCEL:
                executeCancel();
                break;
            case Event.CHAT_ACTIVITY_DOWNLOAD_OK:
                Toast.makeText(
                        context,
                        context.getString(R.string.chat_activity_download_ok) + data[0],
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case Event.CHAT_ACTIVITY_DOWNLOAD_FAILED:
                Toast.makeText(
                        context,
                        context.getString(R.string.chat_activity_download_failed),
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case Event.CHAT_ACTIVITY_SEND_LOCATION:
                executeSendLocation((double)data[0], (double)data[1]);
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

    private void executeSendFile(Uri uri) {
        chatGroupUsecase.execute(
                null,
                uri,
                null,
                "send_file"
        );
    }

    private void executeDownload(int index) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            chatGroupUsecase.execute(
                    null,
                    index,
                    this,
                    "download"
            );
        }
        else {
            ActivityCompat.requestPermissions(
                    (Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0
            );
        }
    }

    private void executeCancel() {
        chatGroupUsecase.execute(
                null,
                null,
                null,
                "cancel"
        );
    }

    private void executeSendLocation(double lat, double lng) {
        chatGroupUsecase.execute(
                null,
                lat,
                lng,
                "send_location"
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
                                new Handler().postDelayed(
                                        () -> publisher.onNext(Event.create(Event.CHAT_ACTIVITY_GOTO_LAST)),
                                        250
                                );
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

    /**
     * Catch event and do action corresponding to item clicked.
     * @param item item clicked.
     * @return {@code true} mean the listener handler this click event.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_activity_menu_send_location:
                publisher.onNext(Event.create(Event.CHAT_ACTIVITY_SHOW_MAP));
                break;
            case R.id.chat_activity_menu_send_contact:
                Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
