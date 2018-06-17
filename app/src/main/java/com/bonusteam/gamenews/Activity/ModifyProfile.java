package com.bonusteam.gamenews.Activity;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonusteam.gamenews.Entity.Favorite;
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ModifyProfile extends AppCompatActivity {
    private TextView nickname,favoriteCount,createdDate,username;
    private CardView changePass;
    private ImageView avatar;
    private GameNewsViewModel viewModel;
    private User currentUser;
    private int noFavorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        }

        avatar = findViewById(R.id.avatar_profile);
        nickname = findViewById(R.id.nickname_main_profile);
        favoriteCount = findViewById(R.id.favorites_count_profile);
        createdDate = findViewById(R.id.created_date_profile);
        username = findViewById(R.id.text_user_profile);
        changePass = findViewById(R.id.btn_change_password_profile);

        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);

        viewModel.refreshCurrentUser();

        viewModel.getFavorieList().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                if (favorites!=null) {
                    noFavorites = favorites.size();
                    favoriteCount.setText(noFavorites+"");
                }
            }
        });
        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
                Picasso.get().load(currentUser.getAvatar()).into(avatar);
                nickname.setText(currentUser.getUsername());
                createdDate.setText(currentUser.getCreateDate());
                username.setText(currentUser.getUsername());
                changePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogPassword = new Dialog(ModifyProfile.this);
                        dialogPassword.setContentView(R.layout.dialog_change_password);
                        dialogPassword.setTitle(R.string.text_change_password);
                        final EditText oldPass = dialogPassword.findViewById(R.id.text_old_password);
                        final EditText newPass = dialogPassword.findViewById(R.id.text_new_password);
                        final EditText repeatPass = dialogPassword.findViewById(R.id.text_repeat_password);
                        Button changePass = dialogPassword.findViewById(R.id.btn_change_password_dialog);

                        changePass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!oldPass.getText().toString().equals("") && !newPass.getText().toString().equals("") && !repeatPass.getText().toString().equals("")){
                                    if(oldPass.getText().toString().equals(currentUser.getPassword())){
                                        if(newPass.getText().toString().equals(repeatPass.getText().toString())){
                                            viewModel.updatePasswordUserAPI(currentUser.get_id(),newPass.getText().toString());
                                            viewModel.refreshCurrentUser();
                                            dialogPassword.dismiss();
                                            Snackbar.make(v,R.string.success_password,Snackbar.LENGTH_SHORT);
                                        }
                                    }
                                }
                            }
                        });
                        dialogPassword.show();
                    }
                });

            }
        });


    }
}
