package com.bonusteam.gamenews.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
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
    private String converImage;
    @ColumnInfo(name = "create_date")
    private String createDate;
    @ColumnInfo(name = "description")
    private String description="--*--";
    @ColumnInfo(name = "body")
    private String body="--*--";
    @NonNull
    @ColumnInfo(name = "game")
    private String game;

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
