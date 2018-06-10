package com.bonusteam.gamenews.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.Favorite;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.CategoryGameDao;
import com.bonusteam.gamenews.Interface.FavoriteDAO;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Interface.PlayerDao;
import com.bonusteam.gamenews.Interface.UserDao;



@Database(entities = {New.class, User.class, Player.class, CategoryGame.class, Favorite.class},version = 1)
public abstract class GameNewsRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewDao newDao();
    public abstract PlayerDao playerDao();
    public abstract CategoryGameDao categoryGameDao();
    public abstract FavoriteDAO favoriteDAO();


    private static GameNewsRoomDatabase INSTANCE;

    public static GameNewsRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (GameNewsRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),GameNewsRoomDatabase.class,"game_news_db")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new CleanUserCache(INSTANCE).execute();
        }
    };

    private static class CleanUserCache extends AsyncTask<User,Void,Void>{

        private UserDao userDao;
        public CleanUserCache(GameNewsRoomDatabase db){
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteAllUser();
            return null;
        }
    }
}
