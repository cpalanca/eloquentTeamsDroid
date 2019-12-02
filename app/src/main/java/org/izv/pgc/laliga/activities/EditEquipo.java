package org.izv.pgc.laliga.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import org.izv.pgc.laliga.EquiposActivity;
import org.izv.pgc.laliga.R;
import org.izv.pgc.laliga.model.data.Equipo;
import org.izv.pgc.laliga.view.EquipoViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static org.izv.pgc.laliga.EquiposActivity.URL_CONSTANT;

public class EditEquipo extends AppCompatActivity {

    private EditText etNombre, etCiudad, etEstadio, etAforo, etComentarios;
    private Button btSave;
    private ImageView imageView;
    private String nombre, estadio, ciudad, aforo, imagen, nombreImg;
    private Float weight, height;
    private Equipo equipo;
    private Context context;
    private EquipoViewModel viewModel;
    private Uri uriImage;
    private static final int PHOTO_SELECTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipo);
        initBlundeReciver();
        initComponents();
        initEvents();
    }

    private void initBlundeReciver() {
        Bundle equipoFecht = getIntent().getExtras();
        equipo = new Equipo();
        if (equipoFecht != null) {
            equipo = equipoFecht.getParcelable("equipo");

            nombre = equipo.getNombre();
            estadio = equipo.getEstadio();
            ciudad = equipo.getCiudad();
            aforo = equipo.getAforo();
            imagen = "http://" + URL_CONSTANT + "/web/aad/public/upload/"+equipo.getEscudo();

        }
    }

    private void initEvents() {

        //SET THE DATA
        etNombre.setText(nombre);
        etEstadio.setText(estadio);
        etCiudad.setText(ciudad);
        etAforo.setText(aforo);

        viewModel = ViewModelProviders.of(this).get(EquipoViewModel.class);

        if (imagen == null) {
            imageView.setImageResource(R.drawable.real_madrid);
        } else {
            Uri imageUri = Uri.parse(imagen);
            Glide.with(this).load(imageUri).into(imageView);
        }

        //imageView.setImageURI(Uri.parse(imagen));

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = equipo.getId();
                equipo.setId(id);
                equipo.setNombre(etNombre.getText().toString());
                equipo.setCiudad(etCiudad.getText().toString());
                equipo.setEstadio(etEstadio.getText().toString());
                equipo.setAforo(etAforo.getText().toString());
                equipo.setEscudo(nombreImg);
                Log.v("EQUIPO", "" + equipo.toString());
                viewModel.edit(id, equipo);
                finish();
                //volverMain();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void volverMain() {
        Intent intent = new Intent(this, EquiposActivity.class);
        startActivity(intent);
        finish();
    }

    private void initComponents() {
        etNombre = findViewById(R.id.etEquipo);
        etEstadio = findViewById(R.id.etEstadio);
        etCiudad = findViewById(R.id.etCiudad);
        etAforo = findViewById(R.id.etAforo);
        etComentarios = findViewById(R.id.etComentarios);
        btSave = findViewById(R.id.btSave);
        imageView = findViewById(R.id.imageView);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] types = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        startActivityForResult(intent, PHOTO_SELECTED);

        Glide.with(this)
                .load(uriImage)
                .into(imageView);
/*
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");


        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        extension = pickIntent.getType();

        startActivityForResult(chooserIntent, PHOTO_SELECTED);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SELECTED && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            uriImage = uri;
            saveSelectedImageInFile(uri);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSelectedImageInFile(Uri uri) {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                bitmap = null;
            }
        } else {
            try {
                final InputStream in = this.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                //ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                //bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                bitmap = null;
            }
        }
        if(bitmap != null) {
            File file = saveBitmapInFile(bitmap);
            if(file != null) {
                viewModel.upload(file);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private File saveBitmapInFile(Bitmap bitmap) {
        nombreImg = "equipo" + LocalDateTime.now()+ ".jpg";
        File file = new File(getFilesDir(), nombre );
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            file = null;
        }
        return file;
    }
}
