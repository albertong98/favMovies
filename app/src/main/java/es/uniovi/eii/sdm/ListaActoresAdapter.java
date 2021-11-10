package es.uniovi.eii.sdm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.modelo.PeliculaReparto;


public class ListaActoresAdapter extends RecyclerView.Adapter<ListaActoresAdapter.ActoresViewHolder>{
    public interface OnItemClickListener{
        void onItemClick(Actor item);
    }

    private List<Actor> listaActores;
    private final OnItemClickListener listener;

    public ListaActoresAdapter(List<Actor> listaActores,OnItemClickListener listener){
        this.listaActores = listaActores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListaActoresAdapter.ActoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_recycler_view_actor,parent,false);
        return new ListaActoresAdapter.ActoresViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaActoresAdapter.ActoresViewHolder holder, int position) {
        Actor actor = listaActores.get(position);
        holder.bindUser(actor,listener);
    }

    @Override
    public int getItemCount() {
        return listaActores.size();
    }

    public static class ActoresViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView personaje;
        private ImageView imagen;

        public ActoresViewHolder(View itemView){
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.nombreactor);
            personaje = (TextView)itemView.findViewById(R.id.personajepelicula);
            imagen = (ImageView)itemView.findViewById(R.id.imagenactor);
        }

        public void bindUser(final Actor actor,final ListaActoresAdapter.OnItemClickListener listener){
            nombre.setText(actor.getNombre());
            this.personaje.setText(actor.getNombre_personaje());

            String url = actor.getImagen();
            Picasso.get().load(url).into(imagen);

            itemView.setOnClickListener(v -> { listener.onItemClick(actor); });
        }
    }
}
