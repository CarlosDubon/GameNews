package com.bonusteam.gamenews.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.bonusteam.gamenews.API.GameNewsAPI;
import com.bonusteam.gamenews.API.NewsRepoDeserializer;
import com.bonusteam.gamenews.API.Response.NewsResponse;
import com.bonusteam.gamenews.Activity.MainActivity;
import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.NewDao;
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
    private LiveData<List<User>> userList;
    private LiveData<List<New>> newList;

    public GameNewsRepository(Application application){
        GameNewsRoomDatabase db = GameNewsRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        newDao = db.newDao();
        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
        createAPI();
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

    public void createAPI(){
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


}
