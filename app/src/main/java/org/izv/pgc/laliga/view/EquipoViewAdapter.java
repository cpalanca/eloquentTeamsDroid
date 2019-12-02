package org.izv.pgc.laliga.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.izv.pgc.laliga.activities.EditEquipo;
import org.izv.pgc.laliga.JugadoresActivity;
import org.izv.pgc.laliga.R;
import org.izv.pgc.laliga.model.data.Equipo;

import java.util.List;

import static org.izv.pgc.laliga.EquiposActivity.URL_CONSTANT;

public class EquipoViewAdapter extends RecyclerView.Adapter<EquipoViewAdapter.ItemHolder> {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LayoutInflater inflater;
    private List<Equipo> equipoList;
    private EquipoViewModel viewModel;
    private Context context;


    public EquipoViewAdapter(Context context, EquipoViewModel viewModel) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = viewModel;

    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_equipo_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if (equipoList != null) {
            final Equipo current = equipoList.get(position);

            holder.nombre.setText(current.getNombre());
            holder.estadio.setText(current.getEstadio());
            holder.aforo.setText(current.getAforo());
            holder.ciudad.setText(current.getCiudad());
            System.out.println(""+current.toString());
            String urlImage = current.getEscudo();

            if (urlImage != null) {
                urlImage = "http://" + URL_CONSTANT + "/web/aad/public/upload/"+current.getEscudo();
                Log.v("xyzyx", "urlImage: " + urlImage);
            } else {
                urlImage = "android.resource://" + context.getPackageName() + "/" + R.drawable.escudo_default;
            }

            Glide.with(context)
                    .load(urlImage)
                    .apply(new RequestOptions().override(600, 200))
                    .into(holder.escudo);

            //holder.escudo.setImageURI(Uri.parse(current.getEscudo()));

            holder.cardTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JugadoresActivity.class);
                    pref =  context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("idEquipo", ""+current.getId());
                    editor.commit();
                    //intent.putExtra("idequipo", ""+current.getId());
                    context.startActivity(intent);

                }
            });

            holder.btBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("¿Esta seguro de eliminarlo?");
                    dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v("equipo", current.toString());
                            if(viewModel == null)
                                Log.v("viewmodel","viewmodel equipo nulo");
                            Log.v("id","equipo : "+current.getId());
                            viewModel.deleteEquipo(current.getId());
                            notifyItemRemoved(position);
                            equipoList.remove(position);
                            //showUndoSnackbar();
                            Toast.makeText(context, "Se elimino correctamente", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.create().show();
                }
            });

            holder.btEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, EditEquipo.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("equipo", current);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });

        } else {
            holder.nombre.setText("No Teams available");
        }

    }

    @Override
    public int getItemCount() {
        int elements = 0;
        if(equipoList != null){
            elements = equipoList.size();
        }
        return elements;
    }

    public void setData(List<Equipo> equipoList){
        this.equipoList = equipoList;
        notifyDataSetChanged(); //actualizar la listas
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView nombre, estadio, aforo, ciudad;
        private final ImageView escudo, btEditar, btBorrar;
        private final CardView cardTeam;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvEquipo);
            estadio = itemView.findViewById(R.id.tvEstadio);
            aforo = itemView.findViewById(R.id.tvAforo);
            ciudad = itemView.findViewById(R.id.tvCiudad);
            escudo = itemView.findViewById(R.id.imageTeam);
            cardTeam = itemView.findViewById(R.id.cardTeam);
            btEditar = itemView.findViewById(R.id.btEditar);
            btBorrar = itemView.findViewById(R.id.btBorrar);
        }
    }
}