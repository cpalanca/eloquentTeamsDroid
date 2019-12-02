package org.izv.pgc.laliga.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import org.izv.pgc.laliga.activities.EditJugador;
import org.izv.pgc.laliga.JugadoresActivity;
import org.izv.pgc.laliga.R;
import org.izv.pgc.laliga.model.data.Jugador;

import java.util.List;

public class JugadorViewAdapter extends RecyclerView.Adapter<JugadorViewAdapter.ItemHolder> {

    private LayoutInflater inflater;
    private JugadorViewModel viewModel;
    private Context context;
    private List<Jugador> jugadorList;
    private String idEquipo;

    public JugadorViewAdapter(Context context, String idEquipo) {
        viewModel = JugadoresActivity.viewModel;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.idEquipo = idEquipo;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_plantilla_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if (jugadorList != null) {
            final Jugador current = jugadorList.get(position);


                holder.nombre.setText(current.getApellidos()+", "+current.getNombre());
                String urlImage = current.getFoto();
                if (urlImage != null) {
                    urlImage = current.getFoto();
                    Log.v("xyzyx", "urlImage: " + urlImage);
                } else {
                    urlImage = "android.resource://" + context.getPackageName() + "/" + R.drawable.ramos;
                }
                Glide.with(context)
                        .load(urlImage)
                        .apply(new RequestOptions().override(600, 200))
                        .into(holder.foto);
                //holder.foto.setImageURI(Uri.parse(current.getFoto()));
                holder.cardJugador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EditJugador.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("jugador", current);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                final int posiRV = position;
            holder.cardJugador.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("¿Desea eliminar la ficha?");
                    dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v("jugadorcurrent", current.toString());
                            viewModel.delete(current.getId());
                            //notifyDataSetChanged();
                            notifyItemRemoved(posiRV);
                            jugadorList.remove(posiRV);
                            Toast.makeText(context, "Jugador eliminado", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.create().show();
                    return true;
                }
            });

           /* holder.btBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("¿Esta seguro de eliminarlo?");
                    dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v("jugador", current.toString());
                            viewModel.delete(current.getId());
                            notifyDataSetChanged();
                            //notifyItemRemoved(position);
                            //equipoList.remove(position);
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

                    Intent intent = new Intent(context, EditJugador.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("jugador", current);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });*/


        } else {
            holder.nombre.setText("No Players availables");
        }
    }

    @Override
    public int getItemCount() {
        int elements = 0;
        if(jugadorList != null){
            elements = jugadorList.size();
        }
        return elements;
    }

    public void setData(List<Jugador> jugadorList){
        this.jugadorList = jugadorList;
        notifyDataSetChanged(); //actualizar la listas
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private final TextView nombre;
        private final ImageView foto, btEditar, btBorrar;
        private final CardView cardJugador;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            foto = itemView.findViewById(R.id.imgJugador);
            cardJugador = itemView.findViewById(R.id.cardJugador);
            btEditar = itemView.findViewById(R.id.btEditar);
            btBorrar = itemView.findViewById(R.id.btBorrar);
        }
    }
}
