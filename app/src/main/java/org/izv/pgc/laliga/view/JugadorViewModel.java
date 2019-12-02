package org.izv.pgc.laliga.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.izv.pgc.laliga.model.Repository;
import org.izv.pgc.laliga.model.data.Jugador;

import java.util.List;

public class JugadorViewModel extends AndroidViewModel {

    private Repository repository;

    public JugadorViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    private LiveData<List<Jugador>> jugador;

    public List<Jugador> getJugadorList() {
        return repository.getJugadorList();
    }

    public LiveData<List<Jugador>> getLiveJugadorList() {
        return repository.getLiveJugadorList();
    }

    public void add(Jugador jugador) {
        repository.addJugador(jugador);
    }

    public void delete(Long id) {
        repository.deleteJugador(id);
    }

    public void edit(Long id, Jugador jugador) {
        repository.updateJugador(id,jugador);
    }

    public void getLiveJugadorTeam(String idEquipo) {
        repository.getLiveJugadorTeam(idEquipo);
    }
}