package com.bonusteam.gamenews.API.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteResponse {
    @SerializedName("_id")
    private List<String> _id;

    public FavoriteResponse(List<String> _id) {
        this._id = _id;
    }

    public List<String> get_id() {
        return _id;
    }
}
