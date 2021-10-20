package es.uniovi.eii.sdm;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.uniovi.eii.sdm.databinding.ActivityShowMovieBinding;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.ui.ArgumentoFragment;
import es.uniovi.eii.sdm.ui.InfoFragment;
import es.uniovi.eii.sdm.util.Conexion;

public class ShowMovie extends AppCompatActivity {

    private ActivityShowMovieBinding binding;
    private TextView categoria;
    private TextView estreno;
    private TextView duracion;
    private TextView contenido;
    private ImageView fondo;
    private ImageView caratula;

    private Pelicula pelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        pelicula = intent.getParcelableExtra(MainRecycler.PELICULA_SELECCIONADA);

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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
            Picasso.get().load(pelicula.getUrlFondo()).into(fondo);
            InfoFragment infoFragment = new InfoFragment().newInstance(pelicula.getFecha(), pelicula.getUrlCaratula(), pelicula.getDuracion());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,infoFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(pelicula != null){
                switch (item.getItemId()){
                    case R.id.navigation_info:
                        InfoFragment infoFragment = new InfoFragment().newInstance(pelicula.getFecha(), pelicula.getUrlCaratula(), pelicula.getDuracion());
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,infoFragment).commit();
                        return true;
                    case R.id.navigation_argumento:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,new ArgumentoFragment().newInstance(pelicula.getArgumento())).commit();
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_show_movie,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id =item.getItemId();
        if(id == R.id.compartir)
            if(new Conexion(getApplicationContext()).CompruebaConexion())
                compartirPeli();
            else
                Toast.makeText(getApplicationContext(),R.string.error_conexion,Toast.LENGTH_LONG).show();

        return super.onOptionsItemSelected(item);
    }

    public void compartirPeli(){
        Intent itSend = new Intent(Intent.ACTION_SEND);

        itSend.setType("text/plain");

        itSend.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.subject_compartir)+": "+pelicula.getTitulo());
        itSend.putExtra(Intent.EXTRA_TEXT,getString(R.string.titulo)+": "
                        +pelicula.getTitulo()+"\n"+getString(R.string.contenido)
                        +": "+pelicula.getArgumento());
        Intent shareIntent=Intent.createChooser(itSend, null);
        startActivity(shareIntent);
    }
}