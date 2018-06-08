package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.API.Response.UserResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UserRepoDeserializer implements JsonDeserializer<UserResponse> {
    @Override
    public UserResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        UserResponse user = new UserResponse();
        JsonObject object = json.getAsJsonObject();

        user.set_id(object.get("_id").getAsString());
        user.setUsername(object.get("user").getAsString());
        if(object.get("avatar").getAsString()!=null){
            user.setAvatar(object.get("avatar").getAsString());
        }else{
            user.setAvatar("");
        }
        user.setPassword(object.get("password").getAsString());
        user.setCreateDate(object.get("created_date").getAsString());

        JsonElement favListJsonElement = object.get("favoriteNews");
        JsonObject favListJsonObject = favListJsonElement.getAsJsonObject();

        return user;
    }
}
