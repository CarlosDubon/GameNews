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
    @NonNull
    @ColumnInfo(name = "id")
    private String _id;
    @NonNull
    @ColumnInfo(name = "username")
    private String username;
    @NonNull
    @ColumnInfo(name = "avatar")
    private String avatar ="http://noAvatar.jpg";
    @NonNull
    @ColumnInfo(name = "password")
    private String password;
    @NonNull
    @ColumnInfo(name = "create_date")
    private String createDate;
    @Ignore
    private List<String> favoritesNews=null;

    public User(String _id, @NonNull String username, @NonNull String avatar, @NonNull String password, @NonNull String createDate) {
        this._id = _id;
        this.username = username;
        this.avatar = avatar;
        this.password = password;
        this.createDate = createDate;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setCreateDate(@NonNull String createDate) {
        this.createDate = createDate;
    }

    public void setFavoriteNew(String idNew) {
        favoritesNews.add(idNew);
    }

    public String get_id() {
        return _id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getCreateDate() {
        return createDate;
    }

    public List<String> getFavoritesNews() {
        return favoritesNews;
    }
}
