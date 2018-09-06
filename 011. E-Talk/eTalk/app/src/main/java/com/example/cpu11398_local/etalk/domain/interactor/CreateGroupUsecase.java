package com.example.cpu11398_local.etalk.domain.interactor;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateGroupUsecase implements Usecase {

    private Context                 context;
    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private ConversationRepository  conversationRepository;

    @Inject
    public CreateGroupUsecase(Context context,
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

    Bitmap       avatar;
    String       name;
    User         user;
    List<String> friends;
    Message      message;
    Conversation conversation;
    DisposableSingleObserver<Boolean> mObserver;

    @Override
    public void execute(Object observer, Object... params) {
        avatar  = (Bitmap)params[0];
        name    = (String)params[1];
        user    = (User)params[2];
        friends = (List<String>)params[3];
        message = new Message(
                user.getUsername(),
                context.getString(R.string.app_first_message),
                Message.TEXT
        );
        conversation = new Conversation(
                Conversation.GROUP,
                name,
                null,
                user.getUsername(),
                message.getTime(),
                new HashMap<String, Long>() {{
                    put(user.getUsername(), 0L);
                    for (String friend : friends) {
                        put(friend, 0L);
                    }
                }},
                message
        );
        mObserver = (DisposableSingleObserver<Boolean>)observer;
        if (avatar == null) {
            pushMessage();
        } else {
            disposable.add(
                    conversationRepository
                            .uploadNetworkGroupAvatar(
                                    conversation.getCreator() + conversation.getTime(),
                                    avatar
                            )
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribe(url -> {
                                conversation.setAvatar(url);
                                pushMessage();
                            })
            );
        }
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
        Single<Boolean> result = conversationRepository.pushNetworkRelationship(
                user.getUsername(),
                conversation
        );
        for (String friend : friends) {
            result = result.zipWith(
                    conversationRepository.pushNetworkRelationship(
                            friend,
                            conversation
                    ),
                    (isSuccess1, isSuccess2) -> isSuccess1 && isSuccess2
            );
        }
        disposable.add(
                result
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