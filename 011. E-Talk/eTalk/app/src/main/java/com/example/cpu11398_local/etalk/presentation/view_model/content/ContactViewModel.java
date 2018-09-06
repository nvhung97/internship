package com.example.cpu11398_local.etalk.presentation.view_model.content;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts.ContactAdapter;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class ContactViewModel extends BaseObservable implements ViewModel{

    /**
     * Current user to get friend from conversation.
     */
    private User currentUser;

    /**
     * Container contain current friend.
     */
    private List<User> friends = new ArrayList<>();

    /**
     * Container contain conversation with key as friend.
     */
    private Map<String, Conversation> conversationMap = new HashMap<>();

    /**
     * Binding {@code adapter} and {@code RecyclerView} for contacts on view.
     */
    private ContactAdapter adapter = new ContactAdapter(friends, new ActionCallback() {
        @Override
        public void chatWith(User user) {
            publisher.onNext(Event.create(Event.CONTACT_FRAGMENT_CHAT));
        }

        @Override
        public void voiceCallWith(User user) {
            Toast.makeText(context, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void videoCallWith(User user) {
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
     * ViewModel use {@code getUserInfoUsecase} to load user info.
     */
    private Usecase getUserInfoUsecase;

    /**
     * ViewModel use {@code loadFriendConversationUsecase} to load friend conversation info.
     */
    private Usecase loadFriendConversationUsecase;

    /**
     * ViewModel use {@code findFriendUsecase} to load friend info.
     */
    private Usecase findFriendUsecase;

    /**
     * Create new {@code ContactViewModel} with a {@code Context}, a {@code ViewModelCallback} and
     * an usecase for loading user info.
     */
    @Inject
    public ContactViewModel(Context context,
                            ViewModelCallback viewModelCallback,
                            @Named("GetUserInfoUsecase") Usecase getUserInfoUsecase,
                            @Named("LoadFriendConversationUsecase") Usecase loadFriendConversationUsecase,
                            @Named("FindFriendUsecase") Usecase findFriendUsecase) {
        this.context                        = context;
        this.viewModelCallback              = viewModelCallback;
        this.getUserInfoUsecase             = getUserInfoUsecase;
        this.loadFriendConversationUsecase  = loadFriendConversationUsecase;
        this.findFriendUsecase              = findFriendUsecase;
    }

    /**
     * Called when view subscribe an observer to this viewModel.
     * @param observer listen event from ViewModel
     */
    @Override
    public void subscribeObserver(Observer<Event> observer) {
        publisher.subscribe(observer);
        if (currentUser == null) {
            getUserInfoUsecase.execute(new GetUserInfoObserver(), false);
        }
    }

    /**
     * Called when this viewModel destroyed to inform usecase stop current task.
     */
    @Override
    public void endTask() {
        getUserInfoUsecase.endTask();
        loadFriendConversationUsecase.endTask();
        findFriendUsecase.endTask();
    }

    /**
     * {@code getUserInfoObserver} is subscribed to usecase to listen event from it.
     */
    private class GetUserInfoObserver extends DisposableSingleObserver<User> {

        @Override
        public void onSuccess(User user) {
            currentUser = user;
            loadFriendConversationUsecase.execute(
                    new LoadFriendConversationObserver(),
                    user.getUsername()
            );
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * {@code LoadFriendConversationObserver} is subscribed to usecase to listen event from it.
     */
    private class LoadFriendConversationObserver extends DisposableObserver<Conversation> {
        @Override
        public void onNext(Conversation conversation) {
            for (String key : conversation.getMembers().keySet()) {
                if (!key.equals(currentUser.getUsername())) {
                    if (!conversationMap.keySet().contains(key)) {
                        findFriendUsecase.execute(
                                new FindFriendObserver(conversation),
                                key,
                                "username"
                        );
                    }
                    break;
                }
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
     * {@code FindFriendObserver} is subscribed to usecase to listen event from it.
     */
    private class FindFriendObserver extends DisposableSingleObserver<Optional<User>> {

        private Conversation conversation;

        public FindFriendObserver(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void onSuccess(Optional<User> user) {
            if (user.isPresent()) {
                friends.add(user.get());
                conversationMap.put(user.get().getUsername(), conversation);
                adapter.notifyItemInserted(friends.size() - 1);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("eTalk", e.getMessage());
        }
    }

    /**
     * A callback to get action when user click item on {@code RecyclerView}.
     */
    public interface ActionCallback {
        void chatWith(User user);
        void voiceCallWith(User user);
        void videoCallWith(User user);
    }
}
