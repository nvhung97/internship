package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages.MessageAdapter;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MessageViewModel extends BaseObservable implements ViewModel {

    /**
     * Used to dispose observer when activity destroyed.
     */
    private Disposable disposable;

    /**
     * Used to determine if user read new messages in the conversation.
     */
    private User currentUser;

    /**
     * Container contain current friend.
     */
    private Map<String, User> friends = new HashMap<>();

    /**
     * Container contain friend conversations.
     */
    private List<Conversation> conversations = new ArrayList<>();

    /**
     * Binding {@code adapter} and {@code RecyclerView} for contacts on view.
     */
    private MessageAdapter adapter = new MessageAdapter(currentUser, conversations, friends, new ActionCallback() {
        @Override
        public void chatWith(Conversation conversation, User friend) {
            publisher.onNext(Event.create(
                    Event.MESSAGE_FRAGMENT_CHAT,
                    currentUser,
                    conversation,
                    friend
            ));
        }
    });

    @Bindable
    public MessageAdapter getAdapter() {
        return adapter;
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
     * Listener from viewModel parent.
     */
    private ViewModelCallback viewModelCallback;

    /**
     * Create new {@code MessageViewModel} with a {@code Context}, a {@code ViewModelCallback}.
     */
    @Inject
    public MessageViewModel(Context context,
                            ViewModelCallback viewModelCallback) {
        this.context                        = context;
        this.viewModelCallback              = viewModelCallback;
        this.viewModelCallback.onChildViewModelSubscribeObserver(
                new MessageObserver(),
                ViewModelCallback.MESSAGES
        );
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
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    /**
     * {@code GroupObserver} is subscribed to parent viewModel to listen event from it.
     */
    private class MessageObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CONTENT_ACTIVITY_EMIT_DATA:
                    currentUser     = (User)data[0];
                    conversations   = (List<Conversation>)data[1];
                    friends         = new HashMap<>((Map<String, User>)data[2]);
                    friends.put(currentUser.getUsername(), currentUser);
                    adapter.onNewData(
                            currentUser,
                            conversations,
                            friends
                    );
                    publisher.onNext(Event.create(Event.MESSAGE_FRAGMENT_HIDE_PROGRESS_BAR));
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * A callback to get action when user click item on {@code RecyclerView}.
     */
    public interface ActionCallback {
        void chatWith(Conversation conversation, User friend);
    }
}
