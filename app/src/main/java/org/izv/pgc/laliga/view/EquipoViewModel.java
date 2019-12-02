package org.izv.pgc.laliga.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.izv.pgc.laliga.model.Repository;
import org.izv.pgc.laliga.model.data.Equipo;

import java.io.File;
import java.util.List;

public class EquipoViewModel extends AndroidViewModel {

    private Repository repository;

    public EquipoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    private LiveData<List<Equipo>> equipos;

    public List<Equipo> getEquipoList() {
        return repository.getEquipoList();
    }

    public LiveData<List<Equipo>> getLiveEquipoList() {
        return repository.getLiveEquipoList();
    }

    public void addEquipo(Equipo equipo) {
        repository.addEquipo(equipo);
    }

    public void deleteEquipo(long id) {
        repository.deleteEquipo(id);
    }

    public void edit(Long id, Equipo equipo) {
        repository.updateEquipo(id,equipo);
    }

    public void setUrl(String url){ repository.setUrl(url); }

    public void upload(File file) { repository.upload(file);
    }
}


