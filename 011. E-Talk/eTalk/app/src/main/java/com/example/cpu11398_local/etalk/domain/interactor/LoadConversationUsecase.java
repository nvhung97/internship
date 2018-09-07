package com.example.cpu11398_local.etalk.domain.interactor;

import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoadConversationUsecase implements Usecase {

    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private ConversationRepository  conversationRepository;

    @Inject
    public LoadConversationUsecase(Executor executor,
                                   Scheduler scheduler,
                                   CompositeDisposable disposable,
                                   ConversationRepository conversationRepository) {
        this.executor               = executor;
        this.scheduler              = scheduler;
        this.disposable             = disposable;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void execute(Object observer, Object... params) {
        disposable.add(
                conversationRepository
                        .loadNetworkRelationships((String)params[0])
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith((DisposableObserver<Conversation>)observer)
        );
    }

    @Override
    public void endTask() {
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }
}