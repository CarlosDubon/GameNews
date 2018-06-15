package com.bonusteam.gamenews.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Entity.CategoryGame;
import com.bonusteam.gamenews.Entity.Favorite;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.SecurityToken;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Fragment.FavoriteNewFragment;
import com.bonusteam.gamenews.Fragment.MainNewsFragment;
import com.bonusteam.gamenews.Fragment.NewsContainerFragment;
import com.bonusteam.gamenews.Fragment.TopPlayerFragment;
import com.bonusteam.gamenews.Interface.NewTools;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainNewsFragment.MainSetters ,
        FavoriteNewFragment.FavoriteNewsTools,
        TopPlayerFragment.TopPlayersTools,
        NewTools{


    public static SecurityToken securityToken;
    private final static int ID_INFLATED_MENU = 101010101;
    private NewsAdapter newsAdapter;
    private GameNewsViewModel viewModel;
    private ImageView avatar;
    private TextView username,created_date;
    private List<CategoryGame> gameList;
    private ActionBar actionBar;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";
    private User currentUser;
    private List<Favorite> idNewList;
    private List<New> favoritesNewList;
    private LinearLayout contentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String value = getApplicationContext().getSharedPreferences("Token",MODE_PRIVATE).getString(TOKEN_SECURITY,"");
        securityToken = new SecurityToken(value);
        Log.d("TOKEN",securityToken.getTokenSecurity());
        contentMain = findViewById(R.id.content_main);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        executeLists();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen_fragment, new MainNewsFragment());
        ft.commit();

    }

    private void executeLists() {
        newsAdapter = new NewsAdapter(this);
        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);

        if(isOnline()) {
            viewModel.refreshNews();
            viewModel.refreshNewsListID();
            viewModel.refreshTopPlayers();
            viewModel.refreshCurrentUser();
            int error = viewModel.getErrorCatcher();
            errorManage(error);
        }else{
            Snackbar message = Snackbar.make(contentMain,"Actualmente no cuenta con una conexion estable a internet, puede que se experimenten problemas",Snackbar.LENGTH_LONG);
            message.getView().setBackgroundColor(Color.rgb(167,20,33));
            TextView textView = (message.getView()).findViewById(android.support.design.R.id.snackbar_action);
            textView.setTextColor(Color.WHITE);
            message.show();

        }

        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    currentUser = user;
                    initProfile(currentUser);
                }
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
        viewModel.getFavorieList().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                if(idNewList!=null) {
                    idNewList.clear();
                }
                idNewList = favorites;
                if (favorites != null) {
                    for(Favorite value:favorites){
                        viewModel.updateNewFaState("1",value.get_id());
                        Log.d("ID_FAVS",value.get_id());
                    }
                }
            }
        });
        viewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                if(newList!=null) {
                    newsAdapter.fillNews(newList);
                }
            }
        });

        viewModel.getFavoriteObjectNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                if(newList!=null) {
                    if(favoritesNewList!=null) {
                        favoritesNewList.clear();
                    }
                    favoritesNewList = newList;
                }
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
            actionBar.setTitle(R.string.app_name);
            fragment = new MainNewsFragment();

        }
        if(id == R.id.favorites){
            fragment = FavoriteNewFragment.newInstance(favoritesNewList);
        }
        if(id == R.id.logout){
            viewModel.deleteAllUsers();
            loggOut();
        }

        int i = 0;
        if(gameList!=null) {
            for (i = 0; i < gameList.size(); i++) {
                if (id == ID_INFLATED_MENU + i) {
                    actionBar.setElevation(0);
                    actionBar.setTitle(gameList.get(i).getCategoryName());
                    fragment = NewsContainerFragment.newInstance(gameList.get(i).getCategoryName());
                    break;
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
    public void initProfile(User user){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username_bar);
        created_date = headerView.findViewById(R.id.date_created_bar);
        avatar = headerView.findViewById(R.id.avatar_user_bar);

        username.setText(user.getUsername());
        created_date.setText(user.getCreateDate());
        Picasso.get().load(user.getAvatar()).into(avatar);

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
    public void addFavorites(String idNew) {
        viewModel.addFavoriteNew(currentUser.get_id(),idNew);
        errorManage(viewModel.getErrorCatcher());
        viewModel.updateNewFaState("1",idNew);
        viewModel.refreshNews();
    }

    @Override
    public void removeFavorites(String idNew) {
        viewModel.removeFavoriteNew(currentUser.get_id(),idNew);
        errorManage(viewModel.getErrorCatcher());
        viewModel.updateNewFaState("0",idNew);
        viewModel.refreshNews();
    }
    @Override
    public void setAdapters(RecyclerView rv) {
        rv.setAdapter(newsAdapter);
    }

    @Override
    public void refreshNews() {
        if(isOnline()) {
            errorManage(viewModel.getErrorCatcher());
            viewModel.refreshNews();

        }else{
            Snackbar message = Snackbar.make(contentMain,"Error al intentar conectar, no se pudo actualizar",Snackbar.LENGTH_LONG);
            message.getView().setBackgroundColor(Color.rgb(167,20,33));
            TextView textView = (message.getView()).findViewById(android.support.design.R.id.snackbar_action);
            textView.setTextColor(Color.WHITE);
            message.show();

        }
    }

    @Override
    public void refreshFavorites() {
        if(isOnline()) {
            errorManage(viewModel.getErrorCatcher());
            viewModel.refreshNewsListID();
            viewModel.refreshCurrentUser();
        }else{
            Snackbar message = Snackbar.make(contentMain,"Error al intentar conectar, no se pudo actualizar",Snackbar.LENGTH_LONG);
            message.getView().setBackgroundColor(Color.rgb(167,20,33));
            TextView textView = (message.getView()).findViewById(android.support.design.R.id.snackbar_action);
            textView.setTextColor(Color.WHITE);
            message.show();

        }
    }

    @Override
    public void refreshTopPlayers() {
        if(isOnline()) {
            errorManage(viewModel.getErrorCatcher());
            viewModel.refreshTopPlayers();
        }else{
            Snackbar message = Snackbar.make(contentMain,"Error al intentar conectar, no se pudo actualizar",Snackbar.LENGTH_LONG);
            message.getView().setBackgroundColor(Color.rgb(167,20,33));
            TextView textView = (message.getView()).findViewById(android.support.design.R.id.snackbar_action);
            textView.setTextColor(Color.WHITE);
            message.show();

        }
    }

    public void timeTokenExceeded(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
        builder.setMessage("Session time exceeded, Please login again")
                .setCancelable(false)
                .setIcon(R.drawable.ic_access_time)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loggOut();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loggOut(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Token",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        startActivity(new Intent(MainActivity.this,LogginActivity.class));
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void errorManage(int error){
        switch (error){
            case 401:
                timeTokenExceeded();
                break;
        }
    }

}
