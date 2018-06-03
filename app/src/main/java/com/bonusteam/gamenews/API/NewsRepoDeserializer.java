package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.API.Response.NewsResponse;
import com.bonusteam.gamenews.Entity.New;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewsRepoDeserializer implements JsonDeserializer<NewsResponse> {
    @Override
    public NewsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        NewsResponse notice = new NewsResponse();

        JsonObject repoJsonObject = json.getAsJsonObject();
        notice.set_id(repoJsonObject.get("_id").getAsString());
        notice.setTitle(repoJsonObject.get("title").getAsString());
        notice.setBody(repoJsonObject.get("body").getAsString());
        notice.setGame(repoJsonObject.get("game").getAsString());
        notice.setCreated_date(repoJsonObject.get("created_date").getAsString());
        //Sospecha que da problemas
        //notice.setCoverImage(repoJsonObject.get("coverImage").getAsString());
        //notice.setDescription(repoJsonObject.get("description").getAsString());

        return notice;
    }
}
