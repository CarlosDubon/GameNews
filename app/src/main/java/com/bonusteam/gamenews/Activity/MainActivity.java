package com.bonusteam.gamenews.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
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

import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.SecurityToken;
import com.bonusteam.gamenews.Fragment.MainNewsFragment;
import com.bonusteam.gamenews.Fragment.NewsContainerFragment;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainNewsFragment.MainSetters{


    public static SecurityToken securityToken;
    private final static int ID_INFLATED_MENU = 101010101;
    private NewsAdapter newsAdapter;
    private GameNewsViewModel viewModel;
    private TextView username,created_date;
    private List<CategoryGame> gameList;
    private ActionBar actionBar;
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

        executeLists();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen_fragment, new MainNewsFragment());
        ft.commit();

    }

    private void executeLists() {
        newsAdapter = new NewsAdapter(this);
        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        viewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                newsAdapter.fillNews(newList);
            }
        });
        viewModel.getGameList().observe(this, new Observer<List<CategoryGame>>() {
            @Override
            public void onChanged(@Nullable List<CategoryGame> categoryGames) {
                if(gameList!=null){
                    gameList.clear();
                }
                gameList = categoryGames;
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
            actionBar.setElevation(8);
            actionBar.setTitle(R.string.app_name);
            fragment = new MainNewsFragment();

        }

        if(gameList!=null) {
            for (int i = 0; i < gameList.size(); i++) {
                if (id == ID_INFLATED_MENU + i) {
                    actionBar.setElevation(0);
                    actionBar.setTitle(gameList.get(i).getCategoryName());
                    fragment = NewsContainerFragment.newInstance(gameList.get(i).getCategoryName());
                }
            }
        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.screen_fragment, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void initControls(){
        actionBar = getSupportActionBar();
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
        subMenuGames.clear();
        for(int i=0;i<gameList.size();i++){
            subMenuGames.add(R.id.grup_games,ID_INFLATED_MENU+i,i,gameList.get(i).getCategoryName()).setCheckable(true);
        }
        navigationView.invalidate();
    }


    @Override
    public void setAdapters(RecyclerView rv) {
        rv.setAdapter(newsAdapter);
    }
}
