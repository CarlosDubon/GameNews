package com.bonusteam.gamenews.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bonusteam.gamenews.Entity.Player;

import java.util.List;

@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayer(Player player);

    @Query("SELECT * FROM player_table")
    LiveData<List<Player>> getAllPlayer();

    @Query("SELECT * FROM player_table WHERE game = :game")
    LiveData<List<Player>> getAllPlayer(String game);
}
