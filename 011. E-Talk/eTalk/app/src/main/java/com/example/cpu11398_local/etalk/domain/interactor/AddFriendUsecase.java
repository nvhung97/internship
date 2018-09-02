package com.example.cpu11398_local.etalk.domain.interactor;

import android.content.Context;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.HashMap;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddFriendUsecase implements Usecase {

    private Context                 context;
    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private ConversationRepository  conversationRepository;

    @Inject
    public AddFriendUsecase(Context context,
                            Executor executor,
                            Scheduler scheduler,
                            CompositeDisposable disposable,
                            ConversationRepository conversationRepository) {
        this.context                = context;
        this.executor               = executor;
        this.scheduler              = scheduler;
        this.disposable             = disposable;
        this.conversationRepository = conversationRepository;
    }

    User         user;
    User         friend;
    Message      message;
    Conversation conversation;
    DisposableSingleObserver<Boolean> mObserver;

    @Override
    public void execute(Object observer, Object... params) {
        user   = (User)params[0];
        friend = (User)params[1];
        message = new Message(
                user.getUsername(),
                context.getString(R.string.app_first_message),
                Message.TEXT
        );
        conversation = new Conversation(
                Conversation.PERSON,
                null,
                user.getUsername(),
                message.getTime(),
                new HashMap<String, Long>() {{
                    put(user.getUsername(), 0L);
                    put(friend.getUsername(), 0L);
                }},
                message
        );
        mObserver = (DisposableSingleObserver<Boolean>)observer;
        pushMessage();
    }

    private void pushMessage(){
        disposable.add(
                conversationRepository
                        .pushNetworkMessage(
                                conversation.getCreator() + conversation.getTime(),
                                message
                        )
                        .doOnSuccess(isSuccess -> {
                            if (isSuccess) {
                                pushConversation();
                            }
                        })
                        .subscribeOn(Schedulers.from(executor))
                        .subscribe()
        );
    }

    private void pushConversation(){
        disposable.add(
                conversationRepository
                        .pushNetworkConversation(conversation)
                        .doOnSuccess(isSuccess -> {
                            if (isSuccess) {
                                createRelationship();
                            }
                        })
                        .subscribeOn(Schedulers.from(executor))
                        .subscribe()
        );
    }

    private void createRelationship(){
        disposable.add(
                conversationRepository
                        .pushNetworkRelationship(
                                user.getUsername(),
                                conversation
                        )
                        .zipWith(
                                conversationRepository.pushNetworkRelationship(
                                        friend.getUsername(),
                                        conversation
                                ),
                                (isSuccess1, isSuccess2) -> isSuccess1 && isSuccess2
                        )
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(mObserver)
        );
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}