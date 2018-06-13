package com.bonusteam.gamenews.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bonusteam.gamenews.API.GameNewsAPI;
import com.bonusteam.gamenews.Entity.SecurityToken;
import com.bonusteam.gamenews.R;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogginActivity extends AppCompatActivity {
    private TextView text_error;
    private EditText text_username,text_password;
    private Button btn_iniciarSesion;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GameNewsAPI api;
    private SecurityToken securityToken;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String savedToken = getApplicationContext().getSharedPreferences("Token",Context.MODE_PRIVATE).getString(TOKEN_SECURITY,"");
        if(!savedToken.equals("")){
            securityToken  = new SecurityToken(savedToken);
            Intent i = new Intent(LogginActivity.this,MainActivity.class);
            //i.putExtra("SECURITY_TOKEN",securityToken);
            startActivity(i);
        }
        setContentView(R.layout.activity_loggin);
        text_username = findViewById(R.id.text_username_loggin);
        text_password = findViewById(R.id.text_password_loggin);
        btn_iniciarSesion = findViewById(R.id.btn_loggin);
        text_error = findViewById(R.id.text_error);
        api = createAPI();
        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();
                compositeDisposable.add(api.getSecurityToken(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getTokenSecurity()));

            }
        });

    }

    private GameNewsAPI createAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameNewsAPI.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GameNewsAPI.class);
    }

    private DisposableSingleObserver<SecurityToken> getTokenSecurity(){
        return new DisposableSingleObserver<SecurityToken>() {
            @Override
            public void onSuccess(SecurityToken value) {
                securityToken = value;
                SharedPreferences shared = LogginActivity.this.getApplicationContext().getSharedPreferences("Token",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString(TOKEN_SECURITY,securityToken.getTokenSecurity());
                editor.apply();
                Intent i = new Intent(LogginActivity.this,MainActivity.class);
                i.putExtra("SECURITY_TOKEN",securityToken);
                startActivity(i);
            }

            @Override
            public void onError(Throwable e) {
                text_error.setVisibility(View.VISIBLE);
            }
        };
    }
}
