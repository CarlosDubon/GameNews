package com.bonusteam.gamenews.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Interface.NewTools;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

public class SingleNewActivity extends AppCompatActivity {
    private User currentUser;
    private String idNew;
    private GameNewsViewModel model;
    private New notice;
    private ImageView imageView;
    private TextView title,game,date,body;
    private FloatingActionButton favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_new);

        Intent responseIntent = getIntent();
        if(responseIntent!=null){
            idNew = responseIntent.getStringExtra("ID_NEW");
            Log.d("ID_NEW",idNew);
        }
        imageView = findViewById(R.id.imageview_new_single);
        title = findViewById(R.id.text_titulo_single);
        game = findViewById(R.id.text_category_single);
        date = findViewById(R.id.text_date_single);
        body = findViewById(R.id.text_body_new_single);
        favorite = findViewById(R.id.btn_fav_single);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        model = ViewModelProviders.of(this).get(GameNewsViewModel.class);

        model.refreshCurrentUser();
        errorManage(model.getErrorCatcher());
        model.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
                setNoticeInfo();
            }
        });



    }
    public void errorManage(int error){
        switch (error){
            case 401:
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SingleNewActivity.this,R.style.myDialog));
                builder.setMessage("Session time exceeded, Please login again")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_access_time)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear().apply();
                                startActivity(new Intent(SingleNewActivity.this,LogginActivity.class));
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public void setNoticeInfo(){
        model.getNew(idNew).observe(this, new Observer<New>() {
            @Override
            public void onChanged(@Nullable New aNew) {
                notice = aNew;
                if(notice!=null){
                    Picasso.get().load(notice.getCoverImage()).error(R.drawable.ic_videogame_asset_black_24dp).into(imageView);
                    title.setText(notice.getTitle());
                    game.setText(notice.getGame());
                    date.setText(notice.getCreated_date());
                    body.setText(notice.getBody());
                    favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(notice.getFavorite()==0){
                                model.addFavoriteNew(currentUser.get_id(),idNew);
                                errorManage(model.getErrorCatcher());
                                model.updateNewFaState("1",idNew);
                                Snackbar.make(v,R.string.add_fav,Snackbar.LENGTH_LONG).show();
                            }else{
                                model.removeFavoriteNew(currentUser.get_id(),idNew);
                                errorManage(model.getErrorCatcher());
                                model.updateNewFaState("0",idNew);
                                Snackbar.make(v,R.string.remove_fav,Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
