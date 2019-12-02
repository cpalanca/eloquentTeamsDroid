package org.izv.pgc.laliga.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.izv.pgc.laliga.JugadoresActivity;
import org.izv.pgc.laliga.R;
import org.izv.pgc.laliga.model.data.Jugador;
import org.izv.pgc.laliga.view.JugadorViewModel;

public class AddJugador extends AppCompatActivity {
    public JugadorViewModel viewModel;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText etNombre,etCiudad,etPosicion,etDorsal;
    private RatingBar rbPuntuacion;
    private Button btSave;
    private ImageView imageView;
    private Jugador jugador;
    private String extension, imagen, idEquipo;
    private Context context;
    private static final int PHOTO_SELECTED = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jugador);

        pref =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = pref.edit();
        idEquipo =  pref.getString("idEquipo", "");
        //Bundle bundle = getIntent().getExtras();
        //idEquipo = (String) bundle.get("idequipo");

        viewModel = ViewModelProviders.of(this).get(JugadorViewModel.class);
        initComponents();
        initEvents();
    }

    private void initEvents() {

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");


                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                extension = pickIntent.getType();

                startActivityForResult(chooserIntent, PHOTO_SELECTED);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jugador = new Jugador();
                String nombreCompleto[] = etNombre.getText().toString().split(" ");
                if(nombreCompleto.length == 4){
                    jugador.setNombre(nombreCompleto[0]+" "+nombreCompleto[1]);
                    jugador.setApellidos(nombreCompleto[2]+ " " + nombreCompleto[4]);
                }else{
                    if(nombreCompleto.length == 3){
                        jugador.setNombre(nombreCompleto[0]);
                        jugador.setApellidos(nombreCompleto[1]+ " " + nombreCompleto[2]);
                    }else{
                        if(nombreCompleto.length == 2){
                            jugador.setNombre(nombreCompleto[0]);
                            jugador.setApellidos(nombreCompleto[1]);
                        }else{
                            String nombrearray=null;
                            for (int i = 0; i < nombreCompleto.length; i++) {
                                if(nombrearray == null)
                                    nombrearray= nombreCompleto[i];
                                else
                                    nombrearray= nombrearray + " " +nombreCompleto[i];
                            }
                            jugador.setNombre(nombrearray);
                            jugador.setApellidos("");
                        }
                    }
                }
                jugador.setIdequipo(Long.parseLong(idEquipo));
                jugador.setFoto(imagen);
                Log.v("Jugador", "" + jugador.toString());
                viewModel.add(jugador);

                finish();
                //volverEquipo();
            }
        });

    }

    private void volverEquipo() {
        Intent intent = new Intent(this, JugadoresActivity.class);
        intent.putExtra("idequipo", idEquipo);
        startActivity(intent);

    }

    private void initComponents() {
        etNombre = findViewById(R.id.etNombre);
        etCiudad = findViewById(R.id.etCiudad);
        etPosicion = findViewById(R.id.etPosicion);
        etDorsal = findViewById(R.id.etNumero);
        rbPuntuacion = findViewById(R.id.rbPuntuacion);
        btSave = findViewById(R.id.btSave);
        imageView = findViewById(R.id.imageView);
    }
}
