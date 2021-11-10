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

import es.uniovi.eii.sdm.modelo.Pelicula;

public class ListaPeliculasAdapter extends RecyclerView.Adapter<ListaPeliculasAdapter.PeliculaViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(Pelicula item);
    }

    private List<Pelicula> listaPeliculas;
    private final OnItemClickListener listener;

    public ListaPeliculasAdapter(List<Pelicula> listaPeli, OnItemClickListener listener){
        this.listaPeliculas = listaPeli;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se crea la vista con el layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_recycler_view_pelicula,parent,false);
        return new PeliculaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPeliculasAdapter.PeliculaViewHolder holder, int position) {
        //Extrae el elemento indicado
        Pelicula pelicula = listaPeliculas.get(position);
        //asigna valores a los componentes
        holder.bindUser(pelicula,listener);
    }

    @Override
    public int getItemCount() { return listaPeliculas.size(); }

    public static class PeliculaViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo;
        private TextView fecha;
        private ImageView imagen;

        public PeliculaViewHolder(View itemView){
            super(itemView);

            titulo = (TextView)itemView.findViewById(R.id.nombreactor);
            fecha = (TextView)itemView.findViewById(R.id.personajepelicula);
            imagen = (ImageView)itemView.findViewById(R.id.imagenactor);
        }

        public void bindUser(final Pelicula pelicula,final OnItemClickListener listener){
            titulo.setText(pelicula.getTitulo()+" "+pelicula.getFecha());
            fecha.setText(pelicula.getCategoria().getNombre());

            String url = pelicula.getUrlCaratula();
            Picasso.get().load(url).into(imagen);

            itemView.setOnClickListener(v -> { listener.onItemClick(pelicula); });
        }
    }
}
