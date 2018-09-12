package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts.ContactAdapter;
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

public class ContactViewModel extends BaseObservable implements ViewModel {

    /**
     * Used to dispose observer when activity destroyed.
     */
    private Disposable disposable;

    /**
     * Current user to get friend from conversation.
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
    private ContactAdapter adapter = new ContactAdapter(currentUser, conversations, friends, new ActionCallback() {
        @Override
        public void chatWith(Conversation conversation) {
            publisher.onNext(Event.create(Event.CONTACT_FRAGMENT_CHAT));
        }

        @Override
        public void voiceCallWith(Conversation conversation) {
            Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void videoCallWith(Conversation conversation) {
            Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
        }
    });

    @Bindable
    public ContactAdapter getAdapter() {
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
     * Create new {@code ContactViewModel} with a {@code Context}, a {@code ViewModelCallback}.
     */
    @Inject
    public ContactViewModel(Context context,
                            ViewModelCallback viewModelCallback) {
        this.context                        = context;
        this.viewModelCallback              = viewModelCallback;
        this.viewModelCallback.onChildViewModelSubscribeObserver(
                new ContactObserver(),
                ViewModelCallback.CONTACTS
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
     * {@code ContactObserver} is subscribed to parent viewModel to listen event from it.
     */
    private class ContactObserver implements Observer<Event> {
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
                    friends         = (Map<String, User>)data[2];
                    conversations   = sortByName((List<Conversation>)data[1]);
                    adapter.onNewData(
                            currentUser,
                            conversations,
                            friends
                    );
                    break;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private List<Conversation> sortByName(List<Conversation> conversations) {
            conversations.sort((conversation1, conversation2) -> {
                User friend1 = null;
                User friend2 = null;
                for (String key : conversation1.getMembers().keySet()) {
                    if (!key.equals(currentUser.getUsername())) {
                        friend1 = friends.get(key);
                        break;
                    }
                }
                for (String key : conversation2.getMembers().keySet()) {
                    if (!key.equals(currentUser.getUsername())) {
                        friend2 = friends.get(key);
                        break;
                    }
                }
                if (friend1 == null && friend2 == null) {
                    return  0;
                } else if (friend1 == null) {
                    return 1;
                } else if (friend2 == null) {
                    return -1;
                } else {
                    return friend1.getName().compareToIgnoreCase(friend2.getName());
                }
            });
            return conversations;
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
        void chatWith(Conversation conversation);
        void voiceCallWith(Conversation conversation);
        void videoCallWith(Conversation conversation);
    }
}
