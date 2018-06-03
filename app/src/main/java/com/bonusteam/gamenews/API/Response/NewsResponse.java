package com.bonusteam.gamenews.API.Response;

import com.google.gson.annotations.SerializedName;

public class NewsResponse {
    @SerializedName("_id")
    private String _id;
    @SerializedName("title")
    private String title="--*--";
    @SerializedName("coverImage")
    private String coverImage="http://";
    @SerializedName("created_date")
    private String created_date="-";
    @SerializedName("description")
    private String description="--*--";
    @SerializedName("body")
    private String body="--*--";
    @SerializedName("game")
    private String game="";

    public NewsResponse() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
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
