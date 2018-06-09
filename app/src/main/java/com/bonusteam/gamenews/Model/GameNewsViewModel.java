package com.bonusteam.gamenews.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Repository.GameNewsRepository;

import java.util.List;

public class GameNewsViewModel extends AndroidViewModel {
    private GameNewsRepository gameNewsRepository;
    private LiveData<List<New>> newList;
    private LiveData<List<Player>> playerList;
    private LiveData<User> currentUser;

    public GameNewsViewModel(@NonNull Application application) {
        super(application);
        gameNewsRepository = new GameNewsRepository(application);
        newList = gameNewsRepository.getAllNews();
        playerList = gameNewsRepository.getAllPlayer();
    }

    public New[] getFavorites(){
        return gameNewsRepository.getFavList();
    }

    public LiveData<User> getCurrentUser(){
        currentUser = gameNewsRepository.getCurrentUser();
        return currentUser;
    }

    public LiveData<List<New>> getAllNews() {
        return newList;
    }

    public LiveData<List<New>> getNewsByGame(String game){
        newList = gameNewsRepository.getNewsByGame(game);
        return newList;
    }
    public LiveData<New> getNew(String id){
        return gameNewsRepository.getNew(id);
    }

    public LiveData<List<Player>> getPlayersByGame(String game){
        playerList = gameNewsRepository.getPlayersByGame(game);
        return playerList;
    }
    public LiveData<List<Player>> getAllPlayers(){
        playerList = gameNewsRepository.getAllPlayer();
        return playerList;
    }

    public LiveData<List<CategoryGame>> getGameList() {
        LiveData<List<CategoryGame>> gameList = gameNewsRepository.getAllGames();
        return gameList;
    }
}
