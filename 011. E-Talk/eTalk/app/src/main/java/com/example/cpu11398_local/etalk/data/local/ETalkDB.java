package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import com.example.cpu11398_local.etalk.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatGroupUsecase.MessagesGroupHolder;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.ChatPersonUsecase.MessagesPersonHolder;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Single;

@Database(
        entities = {
                RoomMessagesPersonHolder.class,
                RoomMessagesGroupHolder.class,
                RoomUser.class,
                RoomConversation.class
        },
        version = 1
)
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
    public Single<List<User>> loadAllUser() {
        return eTalkDao().loadAllUser().map(roomUsers -> {
            List<User> userList = new ArrayList<>();
            for (RoomUser roomUser : roomUsers) {
                userList.add(Mapper.RoomUser2User(roomUser));
            }
            return userList;
        });
    }

    @Override
    public Single<User> loadUser(String username) {
        return eTalkDao().loadUser(username).map(Mapper::RoomUser2User);
    }

    @Override
    public void inserUser(User user) {
        new Thread(() ->
                eTalkDao().insertUser(Mapper.User2RoomUser(user))
        ).start();
    }

    @Override
    public void deleteAllUser() {
        new Thread(() ->
                eTalkDao().removeAllUser()
        ).start();
    }

    @Override
    public Single<List<Conversation>> loadAllConversation() {
        return eTalkDao().loadAllConversation().map(roomConversations -> {
            List<Conversation> conversationList = new ArrayList<>();
            for (RoomConversation roomConversation : roomConversations) {
                conversationList.add(Mapper.RoomConversation2Conversation(roomConversation));
            }
            return conversationList;
        });
    }

    @Override
    public Single<Conversation> loadConversation(String conversatonKey) {
        return eTalkDao().loadConversation(conversatonKey).map(Mapper::RoomConversation2Conversation);
    }

    @Override
    public void insertConversation(Conversation conversation) {
        new Thread(() ->
                eTalkDao().insertConversation(Mapper.Conversation2RoomConversation(conversation))
        ).start();
    }

    @Override
    public void deleteAllConversation() {
        new Thread(() ->
                eTalkDao().removeAllConversation()
        ).start();
    }

    @Override
    public Single<MessagesPersonHolder> loadLocalMessagesPersonHolder(ChatPersonUsecase usecase,
                                                                      String conversationKey) {
        return eTalkDao().loadMessagesPersonHolder(conversationKey).map(chatHolder ->
                Mapper.RoomMessagesPersonHolder2MessagesPersonHolder(usecase, chatHolder)
        );
    }

    @Override
    public void putLocalMessagesPersonHolder(String conversationKey, MessagesPersonHolder messagesPersonHolder) {
        new Thread(() ->
                eTalkDao().insertMessagesPersonHolder(Mapper.MessagesPersonHolder2RoomMessagesPersonHolder(
                        conversationKey,
                        messagesPersonHolder)
                )
        ).start();
    }

    @Override
    public void deleteAllMessagesPersonHolder() {
        new Thread(() ->
                eTalkDao().removeAllMessagesPersonHolder()
        ).start();
    }

    @Override
    public Single<MessagesGroupHolder> loadLocalMessagesGroupHolder(ChatGroupUsecase usecase,
                                                                     String conversationKey) {
        return eTalkDao().loadMessagesGroupHolder(conversationKey).map(messagesGroupHolder ->
                Mapper.RoomMessagesGroupHolder2MessagesGroupHolder(usecase, messagesGroupHolder)
        );
    }

    @Override
    public void putLocalMessagesGroupHolder(String conversationKey, MessagesGroupHolder messagesGroupHolder) {
        new Thread(() ->
                eTalkDao().insertMessagesGroupHolder(Mapper.MessagesGroupHolder2RoomMessagesGroupHolder(
                        conversationKey,
                        messagesGroupHolder)
                )
        ).start();
    }

    @Override
    public void deleteAllMessagesGroupHolder() {
        new Thread(() ->
                eTalkDao().removeAllMessagesGroupHolder()
        ).start();
    }
}
