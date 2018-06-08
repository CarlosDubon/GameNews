package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.API.Response.UserResponse;
import com.bonusteam.gamenews.Entity.User;
import com.google.gson.JsonArray;
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
        user.setPassword(object.get("password").getAsString());
        if(object.get("avatar")!=null){
            if(!object.get("avatar").equals("")) {
                user.setAvatar(object.get("avatar").getAsString());
            }else{
                user.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
            }
        }else{
            user.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
        }
        if(object.get("created_date")!=null) {
            user.setCreateDate(object.get("created_date").getAsString());
        }
        if(object.get("favoriteNews")!=null) {
            JsonArray jsonFavsArray = object.get("favoriteNews").getAsJsonArray();
            String[] favorites = new String[jsonFavsArray.size()];
            for (int i = 0; i < favorites.length; i++) {
                if( jsonFavsArray.getAsString()!=null) {
                    favorites[i] = jsonFavsArray.getAsString();
                }else{
                    favorites[i]="";
                }
            }
            user.setFavoriteNew(favorites);
        }else{
            user.setFavoriteNew(new String[0]);
        }

        return user;
    }
}
