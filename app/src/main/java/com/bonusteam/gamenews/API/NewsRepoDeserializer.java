package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.Entity.New;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewsRepoDeserializer implements JsonDeserializer<New> {
    @Override
    public New deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        New notice = new New();

        JsonObject repoJsonObject = json.getAsJsonObject();
        notice.set_id(repoJsonObject.get("_id").getAsString());
        notice.setTitle(repoJsonObject.get("title").getAsString());
        notice.setBody(repoJsonObject.get("body").getAsString());
        notice.setGame(repoJsonObject.get("game").getAsString());
        notice.setConverImage(repoJsonObject.get("coverImage").getAsString());
        notice.setCreateDate(repoJsonObject.get("create_date").getAsString());

        return notice;
    }
}
