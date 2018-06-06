package com.bonusteam.gamenews.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.bonusteam.gamenews.API.GameNewsAPI;
import com.bonusteam.gamenews.API.NewsRepoDeserializer;
import com.bonusteam.gamenews.API.PlayerRepoDeserializer;
import com.bonusteam.gamenews.API.Response.NewsResponse;
import com.bonusteam.gamenews.API.Response.PlayersResponse;
import com.bonusteam.gamenews.Activity.MainActivity;
import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.CategoryGameDao;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Interface.PlayerDao;
import com.bonusteam.gamenews.Interface.UserDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameNewsRepository {
    private GameNewsAPI api;
    private CompositeDisposable disposable = new CompositeDisposable();
    private UserDao userDao;
    private NewDao newDao;
    private PlayerDao playerDao;
    private CategoryGameDao gameDao;
    private LiveData<List<User>> userList;
    private LiveData<List<New>> newList;
    private LiveData<List<Player>> playerList;
    private LiveData<List<CategoryGame>> gameList;

    public GameNewsRepository(Application application){
        GameNewsRoomDatabase db = GameNewsRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        newDao = db.newDao();
        playerDao = db.playerDao();
        gameDao = db.categoryGameDao();

        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
        playerList = playerDao.getAllPlayer();
        gameList = gameDao.getAllCategories();
        createAPI();
    }

    public LiveData<List<CategoryGame>> getAllGames(){
        api = getGamesFromAPI();
        disposable.add(api.getGameList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGameList()));
        return gameList;
    }

    public void insertGame(CategoryGame game){
        new categoryInsertAsyncTask(gameDao).execute(game);
    }

    public LiveData<List<New>> getNewsByGame(String game){
        newList = newDao.getNewsByCategory(game);
        return newList;
    }
    public LiveData<List<Player>> getPlayersByGame(String game){
        playerList = playerDao.getAllPlayerByGame(game);
        return playerList;
    }
    public LiveData<List<Player>> getAllPlayer(){
        api = getPlayersFromAPI();
        disposable.add(api.getAllPlayers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(getPlayerRepoResponse()));
        return playerList;
    }

    public LiveData<List<User>> getAllUsers(){
        return userList;
    }

    public LiveData<List<New>> getAllNews() {
        disposable.add(api.getNewsByRepo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(getNewsRepoObserver()));
        return newList;
    }
    public void insertUser(User user){
        new userInsertAsyncTask(userDao).execute(user);
    }

    public void insertNews(New news){
        new newsInsertAsyncTask(newDao).execute(news);
    }

    public void insertPlayer(Player player){
        new playerInsertAsyncTask(playerDao).execute(player);
    }

    private static class categoryInsertAsyncTask extends AsyncTask<CategoryGame,Void,Void>{
        private CategoryGameDao gameDao;

        public categoryInsertAsyncTask(CategoryGameDao gameDao){
            this.gameDao = gameDao;
        }
        @Override
        protected Void doInBackground(CategoryGame... categoryGames) {
            gameDao.insertCategory(categoryGames[0]);
            return null;
        }
    }

    private static class playerInsertAsyncTask extends AsyncTask<Player,Void,Void>{
        private PlayerDao playerDao;

        public playerInsertAsyncTask(PlayerDao playerDao){
            this.playerDao = playerDao;
        }
        @Override
        protected Void doInBackground(Player... players) {
            playerDao.insertPayer(players[0]);
            return null;
        }
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



    private void createAPI(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(NewsResponse.class,new NewsRepoDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameNewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GameNewsAPI.class);

    }
    private DisposableSingleObserver<List<NewsResponse>> getNewsRepoObserver(){
        return new DisposableSingleObserver<List<NewsResponse>>() {
            @Override
            public void onSuccess(List<NewsResponse> news) {
                if(!news.isEmpty()){
                    for(NewsResponse notice:news){
                        New newNotice = new New();
                        newNotice.set_id(notice.get_id());
                        newNotice.setTitle(notice.getTitle());
                        newNotice.setDescription(notice.getDescription());
                        newNotice.setConverImage(notice.getCoverImage());
                        newNotice.setBody(notice.getBody());
                        newNotice.setCreateDate(notice.getCreated_date());
                        newNotice.setGame(notice.getGame());
                        insertNews(newNotice);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO: ",e.getMessage());
            }
        };
    }

    private GameNewsAPI getPlayersFromAPI(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlayersResponse.class,new PlayerRepoDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameNewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GameNewsAPI.class);
        return api;
    }

    private DisposableSingleObserver<List<PlayersResponse>> getPlayerRepoResponse(){
        return new DisposableSingleObserver<List<PlayersResponse>>() {
            @Override
            public void onSuccess(List<PlayersResponse> players) {
                if(!players.isEmpty()){
                    for(PlayersResponse player:players){
                        Player p = new Player();
                        p.set_id(player.get_id());
                        p.setAvatar(player.getAvatar());
                        p.setBiografia(player.getBiografia());
                        p.setGame(player.getGame());
                        p.setName(player.getName());
                        insertPlayer(p);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO",e.getMessage());
            }
        };
    }

    private GameNewsAPI getGamesFromAPI(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameNewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GameNewsAPI.class);
    }

    private DisposableSingleObserver<List<String>> getGameList(){
        return new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> games) {
                if(!games.isEmpty()){
                    for(String game:games){
                        insertGame(new CategoryGame(game));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_GAME_LIST",e.getMessage());
            }
        };
    }

}
