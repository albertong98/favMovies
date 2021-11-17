package es.uniovi.eii.sdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    ProgressBar progressBar;
    SharedPreferences sharedPreferencesMainRecycler;
    NotificationManager nNotificationManager;
    NotificationCompat.Builder nBuilder;
    float lineasALeer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);
        progressBar = findViewById(R.id.progressBar);

        ConstruirNotificacion("favMovies","Acceso a la BD de peliculas");
        DownLoadFilesTask task = new DownLoadFilesTask();
        task.execute();
      //  FloatingActionButton faButton = findViewById(R.id.floatingActionButton);
       // faButton.setOnClickListener(view -> {
         //   crearPelicula();
        //});
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickOnItem(Pelicula peli){
        Intent intent = new Intent(MainRecycler.this,ShowMovie.class);
        intent.putExtra(PELICULA_SELECCIONADA,peli);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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

    protected void cargarView(){
        sharedPreferencesMainRecycler = PreferenceManager.getDefaultSharedPreferences(this);
        filtroCategoria  = sharedPreferencesMainRecycler.getString("keyCategoria","");
        PeliculasDataSource peliculasDataSource = new PeliculasDataSource(getApplicationContext());
        peliculasDataSource.open();
        if(filtroCategoria != null && !filtroCategoria.isEmpty()){
            listaPeli = peliculasDataSource.getFilteredValorations(filtroCategoria);
        }else
            listaPeli = peliculasDataSource.getAllValorations();
        peliculasDataSource.close();
        listaPeliView = (RecyclerView)findViewById(R.id.recyclerView);
        listaPeliView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaPeliView.setLayoutManager(layoutManager);

        introListaPeliculas();
    }

    public void ConstruirNotificacion(String titulo,String contenido){
        crearNotificationChannel();
        nNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nBuilder = new NotificationCompat.Builder(getApplicationContext(),"M_CH_ID");
        nBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(titulo)
                    .setContentText(contenido);
    }

    private void crearNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name ="CANAL";
            String descripcion = "DESCRIPCION CANAL";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("M_CH_ID",name,importance);
            channel.setDescription(descripcion);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public float lineasFichero(String fichero){
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        PeliculaRepartoDataSource peliculaRepartoDataSource = null;
        float lineas=0.0f;
        try{
            file = getAssets().open(fichero);
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            peliculaRepartoDataSource = new PeliculaRepartoDataSource(getApplicationContext());
            peliculaRepartoDataSource.open();
            bufferedReader.readLine();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
               lineas++;
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
        return lineas;
    }
    public class DownLoadFilesTask{
        private boolean result;
        private float lineasALeer = 0.0f;
        float numeroLineasLeidas = 0.0f;

        private void cargarPeliculas(){
            Pelicula pelicula;
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
                    numeroLineasLeidas++;
                    progressBar.setProgress((int)((numeroLineasLeidas/lineasALeer)*100));
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
                    numeroLineasLeidas++;
                    progressBar.setProgress((int)((numeroLineasLeidas/lineasALeer)*100));
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
                    numeroLineasLeidas++;
                    progressBar.setProgress((int)((numeroLineasLeidas/lineasALeer)*100));
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
                    numeroLineasLeidas++;
                    progressBar.setProgress((int)((numeroLineasLeidas/lineasALeer)*100));
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
        public void execute(){
            final Executor EXECUTOR = Executors.newSingleThreadExecutor();
            final Handler HANDLER = new Handler(Looper.getMainLooper());
            EXECUTOR.execute(new Runnable(){
                @Override
                public void run(){
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(0);
                            progressBar.setIndeterminate(false);

                            lineasALeer = (float)(lineasFichero("peliculas.csv"));
                            lineasALeer = (float)(lineasALeer+lineasFichero("peliculas-reparto.csv"));
                            lineasALeer = (float)(lineasALeer+lineasFichero("reparto.csv"));
                        }
                    });

                    try{
                        cargarPeliculas();
                        cargarReparto();
                        cargarPeliculaReparto();

                        result=true;
                    }catch(Exception e){
                        result=false;
                    }

                    nNotificationManager.notify(001,nBuilder.build());
                    HANDLER.post(() -> {
                       if(result){
                           cargarView();
                           Toast.makeText(getApplicationContext(),"Lista de peliculas actualizada",Toast.LENGTH_LONG).show();
                       }else
                           Toast.makeText(getApplicationContext(),"Error al actualizar peliculas",Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.INVISIBLE);
                    });
                }
            });
        }
    }
}