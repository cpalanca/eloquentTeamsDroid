package org.izv.pgc.laliga.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import org.izv.pgc.laliga.JugadoresActivity;
import org.izv.pgc.laliga.R;
import org.izv.pgc.laliga.model.data.Jugador;
import org.izv.pgc.laliga.view.JugadorViewModel;

public class EditJugador extends AppCompatActivity {
    public JugadorViewModel viewModel;
    private EditText etNombre,etCiudad,etPosicion,etDorsal;
    private RatingBar rbPuntuacion;
    private Button btSave;
    private ImageView imageView;
    private Jugador jugador;
    private long idEquipo, idJugador;
    private String nombreCompleto, imagen;
    private static final int PHOTO_SELECTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jugador);
        initBlundeReciver();
        initComponents();
        initEvents();
    }

    private void initBlundeReciver() {
        Bundle jugadorFecht = getIntent().getExtras();
        jugador = new Jugador();
        if (jugadorFecht != null) {
            jugador = jugadorFecht.getParcelable("jugador");
            idJugador = jugador.getId();
            nombreCompleto = jugador.getNombre()+" "+jugador.getApellidos();
            Log.v("nombreCompleto: ",nombreCompleto);
            imagen = jugador.getFoto();
            idEquipo = jugador.getIdequipo();
            Log.v("jugadorFECHT: ",jugador.toString());

        }
    }

    private void initEvents() {

        //SET THE DATA
        etNombre.setText(nombreCompleto);
        etCiudad.setText("");
        etPosicion.setText("");
        etDorsal.setText("");
        rbPuntuacion.setRating(6f);

        viewModel = ViewModelProviders.of(this).get(JugadorViewModel.class);

        if (imagen == null) {
            imageView.setImageResource(R.drawable.ramos);
        } else {
            Uri imageUri = Uri.parse(imagen);
            Glide.with(this).load(imageUri).into(imageView);
        }

        //imageView.setImageURI(Uri.parse(imagen));


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");


                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    pickIntent.getType();

                    startActivityForResult(chooserIntent, PHOTO_SELECTED);
                }
            });

            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    jugador.setIdequipo(idEquipo);
                    jugador.setFoto(imagen);
                    Log.v("Jugador", "" + jugador.toString());
                    viewModel.edit(idJugador, jugador);
                    finish();
                    //volverEquipo();
                }
            });

        }

    private void volverEquipo() {
        //Log.v("intentid",idEquipo);
        Intent intent = new Intent(this, JugadoresActivity.class);
        intent.putExtra("idequipo", ""+idEquipo);
        startActivity(intent);
        finish();
    }

    private void initComponents() {
        etNombre = findViewById(R.id.etNombre);
        etCiudad = findViewById(R.id.etCiudad);
        etPosicion = findViewById(R.id.etPosicion);
        etDorsal = findViewById(R.id.etDorsal);
        rbPuntuacion = findViewById(R.id.rbPuntuacion);
        btSave = findViewById(R.id.btSave);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SELECTED && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageView);
            imagen = imageUri.toString();

        }
    }
}