package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.API.Response.PlayersResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PlayerRepoDeserializer implements JsonDeserializer<PlayersResponse> {
    @Override
    public PlayersResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PlayersResponse response = new PlayersResponse();
        JsonObject object = json.getAsJsonObject();
        response.set_id(object.get("_id").getAsString());
        if(object.get("name")!=null){
            response.setName(object.get("name").getAsString());
        }else{
            response.setName("--*--");
        }
        if(object.get("biografia")!=null){
            response.setBiografia(object.get("biografia").getAsString());
        }else{
            response.setBiografia("--*--");
        }
        if(object.get("avatar")!=null){
            if(!object.get("avatar").getAsString().equals("http://.../img.png")) {
                response.setAvatar(object.get("avatar").getAsString());
            }
        }else{
            response.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
        }
        response.setGame(object.get("game").getAsString());

        return response;
    }
}
