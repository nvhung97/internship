package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.cpu11398_local.etalk.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesHolder;
import io.reactivex.Single;

@Database(entities = {ChatHolder.class}, version = 3)
@TypeConverters({Converter.class})
public abstract class ETalkDB extends RoomDatabase implements LocalSource{

    public abstract ETalkDao eTalkDao();

    private final static String DATABASE_NAME  = "etalkdb";
    private static ETalkDB db = null;

    public static ETalkDB getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(
                    context,
                    ETalkDB.class,
                    DATABASE_NAME
            ).build();
        }
        return db;
    }

    @Override
    public Single<MessagesHolder> loadLocalMessagesHolder(ChatPersonUsecase usecase,
                                                 String conversationKey) {
        return eTalkDao().loadChatHolder(conversationKey).map(chatHolder ->
                Mapper.ChatHolder2MessagesHolder(usecase, chatHolder)
        );
    }

    @Override
    public void putLocalMessagesHolder(String conversationKey, MessagesHolder messagesHolder) {
        new Thread(() ->
                eTalkDao().insertChatHolder(Mapper.MessagesHolder2ChatHolder(
                        conversationKey,
                        messagesHolder)
                )
        ).start();
    }
}
