package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;
import io.reactivex.Single;

@Dao
public interface ETalkDao {

    @Query("SELECT * FROM RoomUser")
    Single<List<RoomUser>> loadAllUser();

    @Query("SELECT * FROM RoomUser WHERE id LIKE :username")
    Single<RoomUser> loadUser(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(RoomUser roomUser);

    @Query("DELETE FROM RoomUser")
    void removeAllUser();



    @Query("SELECT * FROM RoomConversation")
    Single<List<RoomConversation>> loadAllConversation();

    @Query("SELECT * FROM RoomConversation WHERE id LIKE :conversationKey")
    Single<RoomConversation> loadConversation(String conversationKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConversation(RoomConversation roomConversation);

    @Query("DELETE FROM RoomConversation")
    void removeAllConversation();



    @Query("SELECT * FROM RoomMessagesPersonHolder WHERE id LIKE :conversationKey")
    Single<RoomMessagesPersonHolder> loadMessagesPersonHolder(String conversationKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesPersonHolder(RoomMessagesPersonHolder roomMessagesPersonHolder);

    @Query("DELETE FROM RoomMessagesPersonHolder")
    void removeAllMessagesPersonHolder();



    @Query("SELECT * FROM RoomMessagesGroupHolder WHERE id LIKE :conversationKey")
    Single<RoomMessagesGroupHolder> loadMessagesGroupHolder(String conversationKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesGroupHolder(RoomMessagesGroupHolder messagesGroupHolder);

    @Query("DELETE FROM RoomMessagesGroupHolder")
    void removeAllMessagesGroupHolder();
}
