package com.bonusteam.gamenews.Interface;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bonusteam.gamenews.Entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);
    @Update
    void modifyUser(User... users);
    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUser(String username);
}
