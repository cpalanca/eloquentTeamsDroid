package org.izv.pgc.laliga.model.rest;

import org.izv.pgc.laliga.model.data.Equipo;
import org.izv.pgc.laliga.model.data.Jugador;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiClient {

    // OPERACIONES DE JUGADORES

    @DELETE("jugador/{id}")
    Call<Boolean> deleteJugador(@Path("id") long id);

    @GET("jugador/{id}")
    Call<Jugador> getJugador(@Path("id") long id);

    @GET("jugador")
    Call<ArrayList<Jugador>> getJugadores();

    @POST("jugador")
    Call<Long> postJugador(@Body Jugador jugador);

    @PUT("jugador/{id}")
    Call<Boolean> putJugador(@Path("id") long id, @Body Jugador jugador);

    @GET("jugador/equipo/{id}")
    Call<ArrayList<Jugador>> getJugadoresTeam(@Path("id") long id);



    //OPERACIONES DE EQUIPOS

    @DELETE("equipo/{id}")
    Call<Boolean> deleteEquipo(@Path("id") long id);

    @GET("equipo/{id}")
    Call<Equipo> getEquipo(@Path("id") long id);

    @GET("equipo")
    Call<ArrayList<Equipo>> getEquipos();

    @POST("equipo")
    Call<Long> postEquipo(@Body Equipo equipo);

    @PUT("equipo/{id}")
    Call<Long> putEquipo(@Path("id") long id, @Body Equipo equipo);

    //SUBIDA DE ARCHIVOS
    @Multipart
    @POST("upload")
    Call<Integer> fileUpload(@Part MultipartBody.Part file);




}
