package com.bonusteam.gamenews.API.Response;

import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse  {
    @SerializedName("_id")
    private String _id;
    @SerializedName("user")
    private String username;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("password")
    private String password;
    @SerializedName("created_date")
    private String createDate;
    @SerializedName("favoriteNews")
    private List<String> favoritesNews=null;

    public UserResponse() {
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

    public void setFavoriteNew(List<String> idNew) {
        favoritesNews = idNew;
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
