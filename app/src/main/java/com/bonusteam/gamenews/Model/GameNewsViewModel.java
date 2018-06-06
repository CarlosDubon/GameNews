package com.bonusteam.gamenews.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Repository.GameNewsRepository;

import java.util.List;

public class GameNewsViewModel extends AndroidViewModel {
    private GameNewsRepository gameNewsRepository;
    private LiveData<List<User>> userList;
    private LiveData<List<New>> newList;
    private LiveData<List<Player>> playerList;
    public GameNewsViewModel(@NonNull Application application) {
        super(application);
        gameNewsRepository = new GameNewsRepository(application);
        userList = gameNewsRepository.getAllUsers();
        newList = gameNewsRepository.getAllNews();
        playerList = gameNewsRepository.getAllPlayer();
    }

    public LiveData<List<Player>> getAllPlayers(){
        return playerList;
    }
    public LiveData<List<New>> getAllNews() {
        return newList;
    }
    public LiveData<List<User>> getAllUsers() {
        return userList;
    }
    public LiveData<List<New>> getNewsByGame(String game){
        newList = gameNewsRepository.getNewsByGame(game);
        return newList;
    }
    public void insertUser(User user){
        gameNewsRepository.insertUser(user);
    }

    public void insertNew(New newObj){
        gameNewsRepository.insertNews(newObj);
    }
}
