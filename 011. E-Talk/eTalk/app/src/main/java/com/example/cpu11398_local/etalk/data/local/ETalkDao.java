package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import io.reactivex.Single;

@Dao
public interface ETalkDao {

    @Query("SELECT * FROM ChatHolder WHERE id LIKE :conversationKey")
    Single<ChatHolder> loadChatHolder(String conversationKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChatHolder(ChatHolder chatHolder);
}
