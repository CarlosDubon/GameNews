package com.bonusteam.gamenews.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bonusteam.gamenews.API.GameNewsAPI;
import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.SecurityToken;
import com.bonusteam.gamenews.Fragment.MainNewsFragment;
import com.bonusteam.gamenews.Fragment.ViewGameNewsFragment;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ViewGameNewsFragment.OnFragmentInteractionListener,
        MainNewsFragment.MainSetters,
        MainNewsFragment.OnFragmentInteractionListener{


    public static SecurityToken securityToken;
    private final static int ID_INFLATED_MENU = 101010101;
    private GameNewsAPI api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private GameNewsViewModel viewModel;
    private TextView username,created_date;
    private List<CategoryGame> gameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        if(i!=null) {
            securityToken = i.getParcelableExtra("SECURITY_TOKEN");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initControls();

        newsAdapter = new NewsAdapter(this);
        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        viewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                newsAdapter.fillNews(newList);
            }
        });
        executeGameList();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen_fragment, new MainNewsFragment());
        ft.commit();

    }

    private void executeGameList() {
        viewModel.getGameList().observe(this, new Observer<List<CategoryGame>>() {
            @Override
            public void onChanged(@Nullable List<CategoryGame> categoryGames) {
                gameList = categoryGames;
                Log.d("LISTA GAMES",gameList.toString());
                addMenuItemInNavMenuDrawer();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if(id==R.id.News_menu){
            fragment = new MainNewsFragment();
            getSupportActionBar().setElevation(8);
            getSupportActionBar().setTitle(R.string.app_name);
        }
        if(gameList!=null) {
            for (int i = 0; i < gameList.size(); i++) {
                if (id == ID_INFLATED_MENU + i) {
                    getSupportActionBar().setElevation(0);
                    getSupportActionBar().setTitle(gameList.get(i).getCategoryName());
                    fragment = ViewGameNewsFragment.newInstance(gameList.get(i).getCategoryName());
                }
            }
        }

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.screen_fragment,fragment).commit();
        }else{
            Log.d("STATUS","FRAGMENTO NULO");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void initControls(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username_bar);
        created_date = headerView.findViewById(R.id.date_created_bar);
        username.setText(securityToken.getTokenSecurity());

    }

    private void addMenuItemInNavMenuDrawer(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuGames = menu.findItem(R.id.menu_games);
        menuGames.setTitle("Games");
        SubMenu subMenuGames = menuGames.getSubMenu();
        //AÑADIENDO LISTA DE JUEGOS
        for(int i=0;i<gameList.size();i++){
            subMenuGames.add(R.id.grup_games,ID_INFLATED_MENU+i,i,gameList.get(i).getCategoryName()).setCheckable(true);
        }
        navigationView.invalidate();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setAdapters(RecyclerView rv) {
        rv.setAdapter(newsAdapter);
    }
}
