package com.bonusteam.gamenews.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Interface.UserDao;

import java.util.List;

public class GameNewsRepository {
    private UserDao userDao;
    private NewDao newDao;
    private LiveData<List<User>> userList;
    private LiveData<List<New>> newList;

    public GameNewsRepository(Application application){
        GameNewsRoomDatabase db = GameNewsRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
    }

    public LiveData<List<User>> getAllUsers(){
        return userList;
    }

    public LiveData<List<New>> getAllNews() {
        return newList;
    }
    public void insertUser(User user){
        new userInsertAsyncTask(userDao).execute(user);
    }
    public void insertNews(New news){
        new newsInsertAsyncTask(newDao).execute(news);
    }
    private static class userInsertAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao userDao;

        public userInsertAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }
    private  static class newsInsertAsyncTask extends AsyncTask<New,Void,Void>{
        private NewDao newDao;

        public newsInsertAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(New... news) {
            newDao.insertNew(news[0]);
            return null;
        }
    }
}
