package es.uniovi.eii.sdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.datos.ActoresDataSource;
import es.uniovi.eii.sdm.datos.PeliculaRepartoDataSource;
import es.uniovi.eii.sdm.datos.PeliculasDataSource;
import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.modelo.PeliculaReparto;

public class MainRecycler extends AppCompatActivity {
    public static String filtroCategoria = null;

    public static final String PELICULA_SELECCIONADA="pelicula_seleccionada";
    public static final String PELICULA_ADD="pelicula_add";
    private static final int GESTION_PELICULA = 1;
    public static final String caratula_por_defecto="https://image.tmdb.org/t/p/w600_and_h900_bestv2/oh8XmxWlySHgGLlx8QOBmq9k72j.jpg";
    public static final String fondo_por_defecto="https://image.tmdb.org/t/p/original/wzJRB4MKi3yK138bJyuL9nx47y6.jpg";
    public static final String trailer_por_defecto = "https://www.youtube.com/watch?v=9UfIRXjoO3I";

    List<Pelicula> listaPeli;
    List<Actor> listaActor;
    List<PeliculaReparto> listaPeliActor;
    RecyclerView listaPeliView;

    SharedPreferences sharedPreferencesMainRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        FloatingActionButton faButton = findViewById(R.id.floatingActionButton);
        faButton.setOnClickListener(view -> {
            crearPelicula();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferencesMainRecycler = PreferenceManager.getDefaultSharedPreferences(this);
        filtroCategoria  = sharedPreferencesMainRecycler.getString("keyCategoria","");

        if(filtroCategoria != null && !filtroCategoria.isEmpty()) cargarPeliculas(filtroCategoria);
        else cargarPeliculas();
        cargarReparto();
        cargarPeliculaReparto();

        listaPeliView = (RecyclerView)findViewById(R.id.recyclerView);
        listaPeliView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaPeliView.setLayoutManager(layoutManager);

        introListaPeliculas();
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
        PeliculasDataSource peliculasDataSource = null;
        BufferedReader bufferedReader = null;
        try{
            file = getAssets().open("peliculas.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            peliculasDataSource = new PeliculasDataSource(getApplicationContext());
            peliculasDataSource.open();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                if(data != null && data.length>=5){
                    pelicula = data.length > 6 ? new Pelicula(Integer.parseInt(data[0]),data[1],data[2],new Categoria(data[3],""),data[4],data[5],data[6],data[7],data[8]) :
                                                  new Pelicula(Integer.parseInt(data[0]),data[1],data[2],new Categoria(data[3],""),data[4],data[5],caratula_por_defecto,fondo_por_defecto,trailer_por_defecto);
                    //listaPeli.add(pelicula);
                    peliculasDataSource.createpelicula(pelicula);
                }
            }
        } catch (IOException e) {
            Log.e("Error lectura","error al leer fichero peliculas");
            e.printStackTrace();
        }finally {
            peliculasDataSource.close();
            try{ bufferedReader.close(); }catch (IOException e){e.printStackTrace();}
            try{ reader.close(); }catch (IOException e){e.printStackTrace();}
            try{ file.close(); }catch (IOException e){e.printStackTrace();}
        }
    }

    private void cargarPeliculas(String filtro){
        Pelicula pelicula;
        listaPeli = new ArrayList<Pelicula>();
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        PeliculasDataSource peliculasDataSource = null;
        try{
            file = getAssets().open("peliculas.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            peliculasDataSource = new PeliculasDataSource(getApplicationContext());
            peliculasDataSource.open();
            bufferedReader.readLine();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                if(data != null && data.length>=5){
                    if(data[3].equals(filtro)) {
                        pelicula = data.length == 8 ? new Pelicula(Integer.parseInt(data[0]),data[1],data[2],new Categoria(data[3],""),data[4],data[5],data[6],data[7],data[8]) :
                                new Pelicula(Integer.parseInt(data[0]),data[1],data[2],new Categoria(data[3],""),data[4],data[5],caratula_por_defecto,fondo_por_defecto,trailer_por_defecto);
                        //listaPeli.add(pelicula);
                        peliculasDataSource.createpelicula(pelicula);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("Error lectura","error al leer fichero peliculas");
            e.printStackTrace();
        }finally {
            peliculasDataSource.close();
            try{ bufferedReader.close(); }catch (IOException e){e.printStackTrace();}
            try{ reader.close(); }catch (IOException e){e.printStackTrace();}
            try{ file.close(); }catch (IOException e){e.printStackTrace();}
        }
    }
    private void cargarReparto(){
        Actor actor;
        listaActor = new ArrayList<Actor>();
        InputStream file = null;
        InputStreamReader reader = null;
        ActoresDataSource actoresDataSource = null;
        BufferedReader bufferedReader = null;
        try{
            file = getAssets().open("reparto.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            actoresDataSource = new ActoresDataSource(getApplicationContext());
            actoresDataSource.open();
            bufferedReader.readLine();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                if(data != null && data.length==4){
                    actor = new Actor(Integer.parseInt(data[0]),data[1],data[2],data[3]);
                    //listaActor.add(actor);
                    actoresDataSource.createactor(actor);
                }
            }
        } catch (IOException e) {
            Log.e("Error lectura","error al leer fichero peliculas");
            e.printStackTrace();
        }finally {
            actoresDataSource.close();
            try{ bufferedReader.close(); }catch (IOException e){e.printStackTrace();}
            try{ reader.close(); }catch (IOException e){e.printStackTrace();}
            try{ file.close(); }catch (IOException e){e.printStackTrace();}
        }
    }
    private void cargarPeliculaReparto(){
        PeliculaReparto pR;
        listaPeliActor = new ArrayList<PeliculaReparto>();
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        PeliculaRepartoDataSource peliculaRepartoDataSource = null;
        try{
            file = getAssets().open("peliculas-reparto.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            peliculaRepartoDataSource = new PeliculaRepartoDataSource(getApplicationContext());
            peliculaRepartoDataSource.open();
            bufferedReader.readLine();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                if(data != null && data.length==3){
                    pR = new PeliculaReparto(Integer.parseInt(data[0]),Integer.parseInt(data[1]),data[2]);
                    //listaPeliActor.add(pR);
                    peliculaRepartoDataSource.createpeliculareparto(pR);
                }
            }
        } catch (IOException e) {
            Log.e("Error lectura","error al leer fichero peliculas");
            e.printStackTrace();
        }finally {
            peliculaRepartoDataSource.close();
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
        PeliculasDataSource peliculasDataSource = new PeliculasDataSource(getApplicationContext());
        peliculasDataSource.open();
        ListaPeliculasAdapter lpAdapter = new ListaPeliculasAdapter(peliculasDataSource.getAllValorations(), new ListaPeliculasAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(Pelicula item) {
                clickOnItem(item);
            }
        });
        peliculasDataSource.close();
        listaPeliView.setAdapter(lpAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_recycler_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.busquedaRecycler){
            Intent intentSettings = new Intent(MainRecycler.this,SettingsActivity.class);
            startActivity(intentSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
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