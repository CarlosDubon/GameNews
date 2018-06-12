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
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GameNewsAPI {
    String ENDPOINT = "http://gamenewsuca.herokuapp.com";
    /** ENDSPOINTS ABOUT USERS **/

    /** @POST("/login") ENDPOINT
     @FormUrlEncoded ENCABEZADO
     Single<SecurityToken> getSecurityToken(@Field("user")String username,@Field("password")String password); **/
    @POST("/login")
    @FormUrlEncoded
    Single<SecurityToken> getSecurityToken(@Field("user")String username,@Field("password")String password);

    @POST("/users/{idUser}/fav")
    @FormUrlEncoded
    Single<Void> addFavorite(@Path("idUser")String idUser,@Field("new")String idNew);

    @HTTP(method = "DELETE",path = "/users/{idUser}/fav",hasBody = true)
    @FormUrlEncoded
    Single<Void> removeFavorite(@Path("idUser")String idUser,@Field("new")String idNew);

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
