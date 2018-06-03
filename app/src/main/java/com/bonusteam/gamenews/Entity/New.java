package com.bonusteam.gamenews.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "news_table")
public class New {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String _id;
    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title = "--*--";
    @NonNull
    @ColumnInfo(name = "cover_image")
    private String converImage;
    @ColumnInfo(name = "create_date")
    private String createDate="-----";
    @ColumnInfo(name = "description")
    private String description="--*--";
    @ColumnInfo(name = "body")
    private String body="--*--";
    @NonNull
    @ColumnInfo(name = "game")
    private String game;

    @Ignore
    public New(){}

    public New(@NonNull String _id, String title, @NonNull String converImage, String createDate, String description, String body, @NonNull String game) {
        this._id = _id;
        this.title = title;
        this.converImage = converImage;
        this.createDate = createDate;
        this.description = description;
        this.body = body;
        this.game = game;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConverImage(@NonNull String converImage) {
        this.converImage = converImage;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setGame(@NonNull String game) {
        this.game = game;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getConverImage() {
        return converImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public String getGame() {
        return game;
    }
}
