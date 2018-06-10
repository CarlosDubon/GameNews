package com.bonusteam.gamenews.API;

import com.bonusteam.gamenews.API.Response.FavoriteResponse;
import com.bonusteam.gamenews.API.Response.NewsResponse;
import com.bonusteam.gamenews.API.Response.PlayersResponse;
import com.bonusteam.gamenews.API.Response.UserResponse;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.SecurityToken;
import com.bonusteam.gamenews.Entity.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GameNewsAPI {
    String ENDPOINT = "http://gamenewsuca.herokuapp.com";
    /** ENDSPOINTS ABOUT USERS **/

    /** @POST("/login") ENDPOINT
     @FormUrlEncoded ENCABEZADO
     Single<SecurityToken> getSecurityToken(@Field("user")String username,@Field("password")String password); **/
    @POST("/login")
    @FormUrlEncoded
    Single<SecurityToken> getSecurityToken(@Field("user")String username,@Field("password")String password);

    @GET("/users/detail")
    Single<UserResponse> getCurrentUser();

    @GET("/users/detail")
    Single<FavoriteResponse> getFavoritesListUser();

    /**ENDPOINTS ABOUT NEWS**/
    @GET("/news")
    Single<List<NewsResponse>> getNewsByRepo();

    @GET("/news/type/list")
    Single<List<String>> getGameList();

    /**ENDS POINTS ABOUT PLAYERS**/
    @GET("/players")
    Single<List<PlayersResponse>> getAllPlayers();




}
