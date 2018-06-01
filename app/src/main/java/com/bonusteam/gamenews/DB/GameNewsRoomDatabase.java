package com.bonusteam.gamenews.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Interface.UserDao;

@Database(entities = {New.class, User.class},version = 1)
public abstract class GameNewsRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewDao newDao();

    private static GameNewsRoomDatabase INSTANCE;

    public static GameNewsRoomDatabase getDatabase(final Context context){
        if(INSTANCE!=null){
            synchronized (GameNewsRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),GameNewsRoomDatabase.class,"game_news_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
