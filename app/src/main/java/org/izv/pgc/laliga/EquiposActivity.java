package org.izv.pgc.laliga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.izv.pgc.laliga.activities.AddEquipo;
import org.izv.pgc.laliga.model.data.Equipo;
import org.izv.pgc.laliga.view.EquipoViewAdapter;
import org.izv.pgc.laliga.view.EquipoViewModel;

import java.util.List;

public class EquiposActivity extends AppCompatActivity {
    public EquipoViewModel viewModel;
    private EquipoViewAdapter adapter;
    private RecyclerView rvList;
    public static final String URL_CONSTANT = "3.86.147.63";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


                FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(EquiposActivity.this, AddEquipo.class);
               startActivity(intent);
            }
        });

        rvList = findViewById(R.id.rvEquipos);
        rvList.setLayoutManager(new LinearLayoutManager(this));


        viewModel = ViewModelProviders.of(this).get(EquipoViewModel.class);

        viewModel.getLiveEquipoList().observe(this, new Observer<List<Equipo>>() {
            @Override
            public void onChanged(@Nullable final List<Equipo> equipos) {
                adapter.setData(equipos);
            }
        });

        adapter = new EquipoViewAdapter(this,viewModel);
        rvList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );
        String url = sharedPreferences.getString("url", "0.0.0.0");
        //viewModel.setUrl(url);

        adapter.notifyDataSetChanged();
        viewModel.getLiveEquipoList().observe(this, new Observer<List<Equipo>>() {
            @Override
            public void onChanged(@Nullable final List<Equipo> equipos) {
                adapter.setData(equipos);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(EquiposActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
