package com.bonusteam.gamenews.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bonusteam.gamenews.Entity.New;

import java.util.List;

@Dao
public interface NewDao {
    @Query("SELECT * FROM news_table ORDER BY create_date DESC")
    LiveData<List<New>> getAllNews();

    @Query("SELECT * FROM news_table WHERE game = :game ORDER BY create_date DESC")
    LiveData<List<New>> getNewsByCategory(String game);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(New news);

    @Query("SELECT * FROM news_table WHERE id = :id")
    LiveData<New> getNew(String id);

    @Query("DELETE FROM news_table")
    void deleteAll();

    @Query("UPDATE news_table SET favorite = :value WHERE id = :idNew ")
    void updateFavoriteState(int value,String idNew);

    @Query("SELECT * FROM news_table WHERE favorite =1")
    LiveData<List<New>> getFavoritesNews();
}
