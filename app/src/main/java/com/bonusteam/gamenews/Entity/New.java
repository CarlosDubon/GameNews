package com.bonusteam.gamenews.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_table")
public class New {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String _id;
    @ColumnInfo(name = "title")
    private String title = "--*--";
    @NonNull
    @ColumnInfo(name = "cover_image")
    private String coverImage;
    @ColumnInfo(name = "create_date")
    private String created_date ="-----";
    @ColumnInfo(name = "description")
    private String description="--*--";
    @ColumnInfo(name = "body")
    private String body="--*--";
    @NonNull
    @ColumnInfo(name = "game")
    private String game;
    @NonNull
    @ColumnInfo(name = "favorite")
    private int favorite=0;

    @Ignore
    public New(){}

    public New(@NonNull String _id, String title, @NonNull String coverImage, String created_date, String description, String body, @NonNull String game) {
        this._id = _id;
        this.title = title;
        this.coverImage = coverImage;
        this.created_date = created_date;
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

    public void setCoverImage(@NonNull String coverImage) {
        this.coverImage = coverImage;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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

    public String getCoverImage() {
        return coverImage;
    }

    public String getCreated_date() {
        return created_date;
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

    @NonNull
    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(@NonNull int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "New{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", created_date='" + created_date + '\'' +
                ", description='" + description + '\'' +
                ", body='" + body + '\'' +
                ", game='" + game + '\'' +
                '}';
    }
}
