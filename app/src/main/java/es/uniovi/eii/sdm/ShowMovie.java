package es.uniovi.eii.sdm;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.uniovi.eii.sdm.databinding.ActivityShowMovieBinding;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class ShowMovie extends AppCompatActivity {

    private ActivityShowMovieBinding binding;
    private TextView categoria;
    private TextView estreno;
    private TextView duracion;
    private TextView contenido;
    private ImageView fondo;
    private ImageView caratula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Pelicula pelicula = intent.getParcelableExtra(MainRecycler.PELICULA_SELECCIONADA);

        binding = ActivityShowMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        categoria = (TextView)findViewById(R.id.categoria);
        estreno = (TextView)findViewById(R.id.estreno);
        duracion = (TextView)findViewById(R.id.duracion);
        contenido = (TextView)findViewById(R.id.argumento);

        fondo = (ImageView)findViewById(R.id.imagenFondo);
        caratula = (ImageView)findViewById(R.id.caratula);

        if(pelicula!=null) mostrarDatos(pelicula,toolBarLayout);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verTrailer(pelicula.getUrlTrailer());
            }
        });
    }

    private void verTrailer(String urlTrailer){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlTrailer)));
    }

    public void mostrarDatos(Pelicula pelicula,CollapsingToolbarLayout toolBarLayout){
        if(!pelicula.getTitulo().isEmpty()) {
            toolBarLayout.setTitle(pelicula.getTitulo());

            categoria.setText(pelicula.getCategoria().getNombre());
            estreno.setText(pelicula.getFecha());
            duracion.setText(pelicula.getDuracion());
            contenido.setText(pelicula.getArgumento());

            Picasso.get().load(pelicula.getUrlFondo()).into(fondo);
            Picasso.get().load(pelicula.getUrlCaratula()).into(caratula);
        }
    }
}