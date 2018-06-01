package com.bonusteam.gamenews.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Repository.GameNewsRepository;

import java.util.List;

public class GameNewsVewModel extends AndroidViewModel {
    private GameNewsRepository gameNewsRepository;
    private LiveData<List<User>> userList;
    private LiveData<List<New>>  newList;
    public GameNewsVewModel(@NonNull Application application) {
        super(application);
        gameNewsRepository = new GameNewsRepository(application);
        userList = gameNewsRepository.getAllUsers();
        newList = gameNewsRepository.getAllNews();
    }

    public LiveData<List<User>> getAllUsers() {
        return userList;
    }
    public LiveData<List<New>> getAllNews() {
        return newList;
    }
    public void insertUser(User user){
        gameNewsRepository.insertUser(user);
    }
    public void insertNew(New newObj){
        gameNewsRepository.insertNews(newObj);
    }
}
