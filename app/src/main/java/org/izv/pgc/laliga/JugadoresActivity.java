package org.izv.pgc.laliga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.izv.pgc.laliga.activities.AddJugador;
import org.izv.pgc.laliga.model.data.Jugador;
import org.izv.pgc.laliga.view.JugadorViewAdapter;
import org.izv.pgc.laliga.view.JugadorViewModel;

import java.util.List;

public class JugadoresActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static JugadorViewModel viewModel;
    private JugadorViewAdapter adapter;
    private String idEquipo;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bundle bundle = getIntent().getExtras();
        //idEquipo = (String) bundle.get("idequipo");
        //Toast.makeText(this, idEquipo, Toast.LENGTH_SHORT).show();
        pref =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = pref.edit();
        idEquipo =  pref.getString("idEquipo", "");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JugadoresActivity.this, AddJugador.class);
                intent.putExtra("idequipo", ""+idEquipo);
                startActivity(intent);
            }
        });

        RecyclerView rvList = findViewById(R.id.rvPlantilla);
        rvList.setLayoutManager(new GridLayoutManager(this, 3));


        viewModel = ViewModelProviders.of(this).get(JugadorViewModel.class);
        viewModel.getLiveJugadorTeam(idEquipo);
        viewModel.getLiveJugadorList().observe(this, new Observer<List<Jugador>>() {

            @Override
            public void onChanged(@Nullable final List<Jugador> jugadors) {
                adapter.setData(jugadors);
            }
        });

        adapter = new JugadorViewAdapter(this, idEquipo);
        rvList.setAdapter(adapter);
    }



}
