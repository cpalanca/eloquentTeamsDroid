package org.izv.pgc.laliga.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.izv.pgc.laliga.EquiposActivity;
import org.izv.pgc.laliga.model.data.Equipo;
import org.izv.pgc.laliga.model.data.Jugador;
import org.izv.pgc.laliga.model.rest.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.izv.pgc.laliga.EquiposActivity.URL_CONSTANT;

public class Repository {
    private Context context;
    private ApiClient apiClient;
    private String url = URL_CONSTANT;


    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/web/aad/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);

        fetchEquiposList();
        //fetchJugadoresList();
    }



    /////////////////////////////////// EQUIPOS  ///////////////////////////////////

    private List<Equipo> equipoList = new ArrayList<>();
    private MutableLiveData<List<Equipo>> liveEquipoList = new MutableLiveData();

    public void fetchEquiposList() {
        Call<ArrayList<Equipo>> call = apiClient.getEquipos();
        call.enqueue(new Callback<ArrayList<Equipo>>() {
            @Override
            public void onResponse(Call<ArrayList<Equipo>> call, Response<ArrayList<Equipo>> response) {
                Log.v("xyz",response.body().toString());
                equipoList= response.body();
                liveEquipoList.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Equipo>> call, Throwable t) {
                equipoList = new ArrayList<>();
            }
        });
    }

    public List<Equipo> getEquipoList() {
        return equipoList;

    }

    public LiveData<List<Equipo>> getLiveEquipoList(){
        return liveEquipoList;
    }

    public void addEquipo(Equipo equipo) {
        Call<Long> call = apiClient.postEquipo(equipo);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Log.v("xyz", ""+response.body());
                long resultado = response.body();
                if(resultado > 0){
                    fetchEquiposList();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v("xyz", "FALLA");
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    public void deleteEquipo(long id) {
        Call<Boolean> call = apiClient.deleteEquipo(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean resultado = response.body();
                if(resultado){
                    fetchEquiposList();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    public void updateEquipo(long id, Equipo equipo) {
        Call<Long> call = apiClient.putEquipo(id, equipo);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long resultado = response.body();
                if(resultado > 0){
                    fetchEquiposList();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    /////////////////////////////////// JUGADOR  ///////////////////////////////////

    private List<Jugador> jugadorList = new ArrayList<>();
    private MutableLiveData<List<Jugador>> liveJugadorList = new MutableLiveData();

    public void fetchJugadoresList() {
        Call<ArrayList<Jugador>> call = apiClient.getJugadores();
        call.enqueue(new Callback<ArrayList<Jugador>>() {
            @Override
            public void onResponse(Call<ArrayList<Jugador>> call, Response<ArrayList<Jugador>> response) {
                Log.v("xyz",response.body().toString());
                jugadorList= response.body();
                liveJugadorList.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Jugador>> call, Throwable t) {
                jugadorList = new ArrayList<>();
            }
        });
    }

    public List<Jugador> getJugadorList() {
        return jugadorList;

    }

    public LiveData<List<Jugador>> getLiveJugadorList(){
        return liveJugadorList;
    }

    public void addJugador(Jugador jugador) {
        Call<Long> call = apiClient.postJugador(jugador);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long resultado = response.body();
                if(resultado > 0){
                    fetchJugadoresList();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    public void deleteJugador(long id) {
        Call<Boolean> call = apiClient.deleteJugador(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean resultado = response.body();
                if(resultado){
                    fetchJugadoresList();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    public void updateJugador(Long id, Jugador jugador) {
        Call<Boolean> call = apiClient.putJugador(id, jugador);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //Log.v("xyz",""+response.body().toString());
                boolean resultado = response.body();

                if(resultado){
                    fetchJugadoresList();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v("xyz", t.getLocalizedMessage());

            }
        });
    }

    public void getLiveJugadorTeam(String idEquipo) {
        Call<ArrayList<Jugador>> call = apiClient.getJugadoresTeam(Long.parseLong(idEquipo));
        Log.d("EQUIPOPORID",""+idEquipo);
        call.enqueue(new Callback<ArrayList<Jugador>>() {
            @Override
            public void onResponse(Call<ArrayList<Jugador>> call, Response<ArrayList<Jugador>> response) {
                Log.v("xyz",response.body().toString());
                jugadorList= response.body();
                liveJugadorList.setValue(response.body());
                Log.d("EQUIPOPORID",""+liveJugadorList.toString());
            }
            @Override
            public void onFailure(Call<ArrayList<Jugador>> call, Throwable t) {
                jugadorList = new ArrayList<>();
                Log.d("EQUIPOPORID",""+jugadorList.toString());
            }
        });
    }

    //////// SUBIDA DE IMAGENES ////

    public void upload(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<Integer> call = apiClient.fileUpload(request);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.v("SUBIDAIMGResponse", ""+response.body());
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v("SUBIDAIMGFailure", t.getLocalizedMessage());
            }
        });
    }



    /////////////////   SET URL SERVER //////////////////////////

    public void setUrl(String url) {
        retrieveApiClient(url);
    }

    private void retrieveApiClient(String url) {
        this.url = url;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/web/aad/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);

    }

}
