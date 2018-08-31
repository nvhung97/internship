package com.example.cpu11398_local.etalk.presentation.view_model.chat;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class ChatViewModel implements ViewModel {

    /**
     * Binding data between {@code textMessage} and chat {@code EditText} on view.
     */
    public ObservableField<String> textMessage = new ObservableField<>("");

    /**
     * Publisher will emit event to view. View listen these event via a observer.
     */
    private PublishSubject<Event> publisher = PublishSubject.create();

    /**
     * Context is used to get resource or toast something on screen.
     */
    private Context context;

    /**
     * create new {@code ChatViewModel} with a context.
     */
    @Inject
    public ChatViewModel(Context context) {
        this.context = context;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
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
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click send button to send a Message.
     * After send current messgase, reset {@code textMessage} to empty.
     * @param view
     */
    public void onSendMessage(View view) {
        textMessage.set("");
    }

    @Override
    public void endTask() {

    }
}
