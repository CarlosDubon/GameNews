package com.bonusteam.gamenews.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.CategoryGameDao;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Interface.PlayerDao;
import com.bonusteam.gamenews.Interface.UserDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = {New.class, User.class, Player.class, CategoryGame.class},version = 1)
public abstract class GameNewsRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewDao newDao();
    public abstract PlayerDao playerDao();
    public abstract CategoryGameDao categoryGameDao();


    private static GameNewsRoomDatabase INSTANCE;

    public static GameNewsRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (GameNewsRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),GameNewsRoomDatabase.class,"game_news_db")
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback callback= new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };
    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void>{
        private final NewDao dao;

        PopulateDbAsync(GameNewsRoomDatabase db){
            dao = db.newDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            New notice =new New("ntc01","Fortnite presenta su nuevo modo competitivo","https://cdn2.areajugones.es/wp-content/uploads/2018/05/Fortnite-14-810x400.jpg"
            ,dateFormat.format(date)+"","Epic Games continúa dando pequeños pasos en el competitivo de Fortnite y viendo lo que gustó a la comunidad el MTL Enfrentamiento en solitario, han incluido un nuevo modo competitivo, Enfrentamiento Relámpago."
            ,"Enfrentamiento Relámpago estará disponible hasta el lunes 4 de junio a las 4 de la mañana y como bien explica la nota de prensa publicada por la desarrolladora, en este modo los círculos de la tormenta y los combates serán más rápidos. Además a diferencia de Enfrentamiento en solitario las bajas que consigamos si que servirán para conseguir más puntuación."
            ,"Fortnite");
            dao.insertNew(notice);
            return null;
        }
    }
}
