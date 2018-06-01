package com.bonusteam.gamenews.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String _id;
    @NonNull
    @ColumnInfo(name = "username")
    private String username;
    @NonNull
    @ColumnInfo(name = "password")
    private String password;
    @NonNull
    @ColumnInfo(name = "create_date")
    private String createDate;
    @Ignore
    private List<String> favoritesNews=null;

    public User(String _id, @NonNull String username, @NonNull String password, @NonNull String createDate) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.createDate = createDate;
    }

    public void setFavoriteNew(String idNew) {
        favoritesNews.add(idNew);
    }
}
