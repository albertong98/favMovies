package es.uniovi.eii.sdm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class MainRecycler extends AppCompatActivity {
    public static final String PELICULA_SELECCIONADA="pelicula_seleccionada";
    public static final String PELICULA_ADD="pelicula_add";
    private static final int GESTION_PELICULA = 1;

    List<Pelicula> listaPeli;
    RecyclerView listaPeliView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        rellenarLista();

        listaPeliView = (RecyclerView)findViewById(R.id.recyclerView);
        listaPeliView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaPeliView.setLayoutManager(layoutManager);

        introListaPeliculas();

        FloatingActionButton faButton = findViewById(R.id.floatingActionButton);
        faButton.setOnClickListener(view -> {
            crearPelicula();
        });
    }

    public void clickOnItem(Pelicula peli){
        Intent intent = new Intent(MainRecycler.this,MainActivity.class);
        intent.putExtra(PELICULA_SELECCIONADA,peli);
        startActivity(intent);
    }

    private void rellenarLista(){
        listaPeli = new ArrayList<Pelicula>();
        Categoria coches = new Categoria("Coches","Peliculas de coches");
        listaPeli.add(new Pelicula("Fast and Furious: Tokyo drift","coches",coches,"110 min","05/05/2005"));
        listaPeli.add(new Pelicula("2 Fast 2 Furious","coches",coches,"110 min","23/06/2003"));
    }

    private void crearPelicula(){
        Intent peliculaIntent = new Intent(MainRecycler.this,MainActivity.class);
        startActivityForResult(peliculaIntent,GESTION_PELICULA);
    }

    private void introListaPeliculas(){
        ListaPeliculasAdapter lpAdapter = new ListaPeliculasAdapter(listaPeli, new ListaPeliculasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pelicula item) {
                clickOnItem(item);
            }
        });
        listaPeliView.setAdapter(lpAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GESTION_PELICULA){
            if(resultCode == RESULT_OK){
                Pelicula pelicula = data.getParcelableExtra(PELICULA_ADD);
                listaPeli.add(pelicula);
                introListaPeliculas();
            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("FavMovie.MainActivity","CategoriaActivity cancelada");
    }
}