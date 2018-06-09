package com.bonusteam.gamenews.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

public class SingleNewActivity extends AppCompatActivity {

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

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        model = ViewModelProviders.of(this).get(GameNewsViewModel.class);
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
                }
            }
        });

    }
}
