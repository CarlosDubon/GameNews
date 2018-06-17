package com.bonusteam.gamenews.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.bonusteam.gamenews.API.FavoriteDeserializer;
import com.bonusteam.gamenews.API.GameNewsAPI;
import com.bonusteam.gamenews.API.NewsRepoDeserializer;
import com.bonusteam.gamenews.API.PlayerRepoDeserializer;
import com.bonusteam.gamenews.API.Response.FavoriteResponse;
import com.bonusteam.gamenews.API.Response.NewsResponse;
import com.bonusteam.gamenews.API.Response.PlayersResponse;
import com.bonusteam.gamenews.API.Response.UserResponse;
import com.bonusteam.gamenews.API.UserRepoDeserializer;
import com.bonusteam.gamenews.Activity.MainActivity;
import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

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
    private FavoriteDAO favoriteDAO;

    private LiveData<List<User>> userList;
    private LiveData<List<New>> newList;
    private LiveData<List<Player>> playerList;
    private LiveData<List<CategoryGame>> gameList;
    private LiveData<User> currentUser;
    private LiveData<List<Favorite>> favoriteList;

    private int errorCatcher = 200;

    public GameNewsRepository(Application application){
        GameNewsRoomDatabase db = GameNewsRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        newDao = db.newDao();
        playerDao = db.playerDao();
        gameDao = db.categoryGameDao();
        favoriteDAO = db.favoriteDAO();

        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
        playerList = playerDao.getAllPlayer();
        gameList = gameDao.getAllCategories();
        currentUser = userDao.getCurrentUser();
        favoriteList = favoriteDAO.getAllFavorite();
        createAPI();
    }

    /**
     *GETTERS
     */

    public LiveData<List<Favorite>> getAllFavorites(){
        return favoriteList;
    }
    public LiveData<User> getCurrentUser(){
        return currentUser;
    }
    public LiveData<List<CategoryGame>> getAllGames(){
        api = getGamesFromAPI();
        disposable.add(api.getGameList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGameList()));
        return gameList;
    }
    public LiveData<New> getNew(String id){
        return newDao.getNew(id);
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
        return playerList;
    }
    public LiveData<List<User>> getAllUsers(){
        return userList;
    }
    public LiveData<List<New>> getAllNews() {
        return newList;
    }
    public LiveData<List<New>> getFavoritesObjectNews(){
        return newDao.getFavoritesNews();
    }

    public int getErrorCatcher(){
        return errorCatcher;
    }

    /**
     * SETTERS
     */

    public void updateCurrentUser(String idUser,String newPassword){
        api = createAddFavRequest();
        disposable.add(api.updateUser(idUser,newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(updateUserPasswordObserver()));
    }
    public void addFavoriteNew(String idUser,String idNew){
        api = createAddFavRequest();
        disposable.add(api.addFavorite(idUser,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(addFavObserver()));
    }
    public void removeFavoriteNew(String User,String idNew){
        api = createAddFavRequest();
        disposable.add(api.removeFavorite(User,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(removeFavObserver()));
    }
    public void exectInserFavorite(Favorite fab){
        insertFavorite(fab);
    }




    /**
     * Metodos para obtener informacion de la API
     */
    public void refreshCurrentUser(){
        api = getCurrentUserByRepo();
        disposable.add(api.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getUserLogged()));
    }
    public void refreshNews(){
        disposable.add(api.getNewsByRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getNewsRepoObserver()));
    }
    public void refreshFavoritesListID(){
        api = getFavoritesNoticesByRepo();
        disposable.add(api.getFavoritesListUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavoritesObserver()));
    }
    public void refreshTopPlayers(){
        api = getPlayersFromAPI();
        disposable.add(api.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getPlayerRepoResponse()));
    }


    /**
     *METODOS QUE EJECTUTAN LOS THREADS
     */
    public void insertGame(CategoryGame game){
        new categoryInsertAsyncTask(gameDao).execute(game);
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

    private void insertFavorite(Favorite fab) {
        new favoritesInsertAsyncTask(favoriteDAO).execute(fab);
    }

    public void updateFavNewState(String value, String idNew){
        new favoritesUpdateAsyncTask(newDao).execute(value,idNew);
    }
    public void deleteAllFavotitesID(){
        new deleteAllFavotitesIDAsyncTask(favoriteDAO).execute();
    }

    public void deleteAllUsers(){
        new deleteAllUsersAsyncTask(userDao).execute();
    }

    public void updateUserPassword(String idUser,String newPassword){
        new updateUserPasswordAsyncTask(userDao).execute(idUser,newPassword);
    }





    /**
     * CREACION DE THREADS ENCARGADOS DE LA INSERCION DE DATOS EN LA BASE DE DATOS
     */
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
    private static class favoritesInsertAsyncTask extends AsyncTask<Favorite,Void,Void>{

        private FavoriteDAO favoriteDAO;

        public favoritesInsertAsyncTask(FavoriteDAO favoriteDAO){
            this.favoriteDAO = favoriteDAO;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDAO.insertFavorite(favorites[0]);
            return null;
        }
    }
    private static class favoritesUpdateAsyncTask extends AsyncTask<String,Void,Void>{
        private NewDao dao;

        public favoritesUpdateAsyncTask(NewDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... values) {
            Log.d("UPDATE_VALUES",values[0] +" "+ values[1]);
            dao.updateFavoriteState(Integer.parseInt(values[0]),values[1]);
            return null;
        }
    }

    private static class deleteAllFavotitesIDAsyncTask extends AsyncTask<Void,Void,Void>{
        private FavoriteDAO dao;
        public deleteAllFavotitesIDAsyncTask(FavoriteDAO dao){
            this.dao =dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
    private static class deleteAllUsersAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserDao userDao;
        public deleteAllUsersAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUser();
            return null;
        }
    }

    private static class updateUserPasswordAsyncTask extends AsyncTask<String,Void,Void>{
        private UserDao userDao;

        public updateUserPasswordAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            userDao.updateUserPassword(strings[0],strings[1]);
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

                        Response response = chain.proceed(newRequest);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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
                        newNotice.setCoverImage(notice.getCoverImage());
                        newNotice.setBody(notice.getBody());
                        newNotice.setCreated_date(notice.getCreated_date());
                        newNotice.setGame(notice.getGame());
                        insertNews(newNotice);
                    }
                    refreshFavoritesListID();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO_USER: ",e.getMessage());
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
                        Response response = chain.proceed(newRequest);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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
                        Response response = chain.proceed(request);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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

    private GameNewsAPI getCurrentUserByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(UserResponse.class,new UserRepoDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        Response response = chain.proceed(newRequest);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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
    private DisposableSingleObserver<UserResponse> getUserLogged(){
        return new DisposableSingleObserver<UserResponse>() {
            @Override
            public void onSuccess(UserResponse value) {
                User user = new User();
                user.set_id(value.get_id());
                user.setUsername(value.getUsername());
                user.setAvatar(value.getAvatar());
                user.setPassword(value.getPassword());
                user.setCreateDate(value.getCreateDate());
                insertUser(user);
            }

            @Override
            public void onError(Throwable e) {
                //Log.d("ERROR_GET_USER",e.getMessage());
            }
        };
    }

    private GameNewsAPI getFavoritesNoticesByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(FavoriteResponse.class,new FavoriteDeserializer())
                .registerTypeAdapter(NewsResponse.class,new NewsRepoDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        Response response = chain.proceed(newRequest);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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
    private DisposableSingleObserver<FavoriteResponse> getFavoritesObserver(){
        return new DisposableSingleObserver<FavoriteResponse>() {
            @Override
            public void onSuccess(FavoriteResponse values) {
                deleteAllFavotitesID();
                for(String value:values.get_id()){
                    insertFavorite(new Favorite(value));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("FAVORITESIDREPO",e.getMessage());
            }
        };
    }

    private GameNewsAPI createAddFavRequest(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        Response response = chain.proceed(request);
                        if(response.code() != 200){
                            errorCatcher = response.code();
                        }
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
    private DisposableSingleObserver<Void> addFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
    private DisposableSingleObserver<Void> removeFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private DisposableSingleObserver<Void> updateUserPasswordObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

}
