package es.uniovi.eii.sdm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class MainRecycler extends AppCompatActivity {
    public static final String PELICULA_SELECCIONADA="pelicula_seleccionada";
    public static final String PELICULA_ADD="pelicula_add";
    private static final int GESTION_PELICULA = 1;
    public static final String caratula_por_defecto="https://image.tmdb.org/t/p/w600_and_h900_bestv2/oh8XmxWlySHgGLlx8QOBmq9k72j.jpg";
    public static final String fondo_por_defecto="https://image.tmdb.org/t/p/original/wzJRB4MKi3yK138bJyuL9nx47y6.jpg";
    public static final String trailer_por_defecto = "https://www.youtube.com/watch?v=9UfIRXjoO3I";

    List<Pelicula> listaPeli;
    RecyclerView listaPeliView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        cargarPeliculas();

        listaPeliView = (RecyclerView)findViewById(R.id.recyclerView);
        listaPeliView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaPeliView.setLayoutManager(layoutManager);

        introListaPeliculas();

        FloatingActionButton faButton = findViewById(R.id.floatingActionButton);
        faButton.setOnClickListener(view -> {
            crearPelicula();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickOnItem(Pelicula peli){
        Intent intent = new Intent(MainRecycler.this,ShowMovie.class);
        intent.putExtra(PELICULA_SELECCIONADA,peli);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void cargarPeliculas(){
        Pelicula pelicula;
        listaPeli = new ArrayList<Pelicula>();
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;

        try{
            file = getAssets().open("lista_peliculas_url_utf8.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                //Pendiente de arreglar
                if(data != null && data.length>=5){
                    pelicula = data.length == 8 ? new Pelicula(data[0],data[1],new Categoria(data[2],""),data[3],data[4],data[5],data[6],data[7]) :
                                                  new Pelicula(data[0],data[1],new Categoria(data[2],""),data[3],data[4],caratula_por_defecto,fondo_por_defecto,trailer_por_defecto);
                    listaPeli.add(pelicula);
                }
            }
        } catch (IOException e) {
            Log.e("Error lectura","error al leer fichero peliculas");
            e.printStackTrace();
        }finally {
            try{ bufferedReader.close(); }catch (IOException e){e.printStackTrace();}
            try{ reader.close(); }catch (IOException e){e.printStackTrace();}
            try{ file.close(); }catch (IOException e){e.printStackTrace();}
        }
    }

    private void crearPelicula(){
        Intent peliculaIntent = new Intent(MainRecycler.this,MainActivity.class);
        startActivityForResult(peliculaIntent,GESTION_PELICULA);
    }

    private void introListaPeliculas(){
        ListaPeliculasAdapter lpAdapter = new ListaPeliculasAdapter(listaPeli, new ListaPeliculasAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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