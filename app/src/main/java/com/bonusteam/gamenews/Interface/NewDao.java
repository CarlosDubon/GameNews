package com.bonusteam.gamenews.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bonusteam.gamenews.Entity.New;

import java.util.List;

@Dao
public interface NewDao {
    @Query("SELECT * FROM news_table")
    LiveData<List<New>> getAllNews();

    @Query("SELECT * FROM news_table WHERE game = :game")
    LiveData<List<New>> getNewsByCategory(String game);
    @Insert
    void insertNew(New news);
}
