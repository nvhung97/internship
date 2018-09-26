package com.example.cpu11398_local.etalk.data.repository.data_source;

import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesHolder;
import io.reactivex.Single;

public interface LocalSource {

    Single<MessagesHolder> loadLocalMessagesHolder(ChatPersonUsecase usecase, String conversationKey);

    void putLocalMessagesHolder(String conversationKey, MessagesHolder chatHolder);
}
