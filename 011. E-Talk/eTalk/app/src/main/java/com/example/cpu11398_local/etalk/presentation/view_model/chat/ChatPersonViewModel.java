package com.example.cpu11398_local.etalk.presentation.view_model.chat;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.BR;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class ChatPersonViewModel extends BaseObservable implements ViewModel, ViewModelCallback {

    /**
     * Binding data between {@code textName} and chat {@code EditText} on view.
     */
    private String textName = "";

    @Bindable
    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
        notifyPropertyChanged(BR.textName);
    }

    /**
     * Binding data between {@code textStatus} and chat {@code EditText} on view.
     */
    private String textStatus = "";
    private Handler handler = new Handler();

    @Bindable
    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(long time) {
        handler.removeCallbacksAndMessages(null);
        if (System.currentTimeMillis() - time < 10000) {
            this.textStatus = context.getString(R.string.app_online);
            handler.postDelayed(
                    () -> {
                        this.textStatus = context.getString(R.string.app_offline);
                        notifyPropertyChanged(BR.textStatus);
                    },
                    10000 + time - System.currentTimeMillis()
            );
        } else {
            this.textStatus = context.getString(R.string.app_offline);
        }
        notifyPropertyChanged(BR.textStatus);
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
    private Usecase chatPersonUsecase;

    /**
     * create new {@code ChatPersonViewModel} with a context.
     */
    @Inject
    public ChatPersonViewModel(Context context,
                               @Named("ChatPersonUsecase") Usecase chatPersonUsecase) {
        this.context            = context;
        this.chatPersonUsecase  = chatPersonUsecase;
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
        Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when user click send button to send a Message.
     * After send current messgase, reset {@code textMessage} to empty.
     * @param view
     */
    public void onSendMessage(View view) {
        setTextMessage("");
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        chatPersonUsecase.endTask();
    }

    @Override
    public void onChildViewModelSubscribeObserver(Observer<Event> observer, int code) {

    }

    @Override
    public void onHelp(Event event) {
        Object[] data = event.getData();
        switch (event.getType()) {
            case Event.CHAT_ACTIVITY_VALUE:
                setTextName((String)data[2]);
                setTextStatus((long)data[3]);
                chatPersonUsecase.execute(
                        new ChatObserver(),
                        data[0],
                        data[1],
                        "first_load"
                );
                Log.e("Test", (String)data[0] + " " + (String)data[1]);
        }
    }

    /**
     * {@code LoadContentDataObserver} is subscribed to usecase to listen event from it.
     */
    private class ChatObserver extends DisposableObserver<Event> {

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CHAT_ACTIVITY_FRIEND:
                    User friend = (User)data[0];
                    setTextName(friend.getName());
                    setTextStatus(friend.getActive());
                    break;
                case Event.CHAT_ACTIVITY_MESSAGES:
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
