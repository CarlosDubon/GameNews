package com.bonusteam.gamenews.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bonusteam.gamenews.Entity.Favorite;
import com.bonusteam.gamenews.Entity.New;

import java.util.List;

@Dao
public interface FavoriteDAO {
    @Query("SELECT * FROM favorites_table")
    LiveData<List<Favorite>> getAllFavorite();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Favorite favorite);

    @Query("DELETE FROM favorites_table")
    void deleteAll();
}
