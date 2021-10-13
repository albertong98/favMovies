package es.uniovi.eii.sdm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class MainActivity extends AppCompatActivity {
    private Snackbar msgCreaCategoria;

    public static final String CATEGORIA_SELECCIONADA = "categoria_seleccionada";
    public static final String CATEGORIA_MODIFICADA = "categoria_modificada";
    public static final String POS_CATEGORIA_SELECCIONADA = "pos_categoria_seleccionada";

    private static final int GESTION_CATEGORIA = 1;

    private List<Categoria> listaCategorias;
    private Spinner spinner;
    private EditText titulo;
    private EditText argumento;
    private EditText duracion;
    private boolean creandoCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Pelicula peliculaSeleccionada = intent.getParcelableExtra(MainRecycler.PELICULA_SELECCIONADA);


        listaCategorias = new ArrayList<Categoria>();
        listaCategorias.add(new Categoria("Accion","Peliculas de acción"));
        listaCategorias.add(new Categoria("Comedia","Peliculas de comedia"));
        listaCategorias.add(new Categoria("Coches","Peliculas de coches"));

        titulo = (EditText)findViewById(R.id.editTitulo);
        argumento = (EditText)findViewById(R.id.editArgumento);
        duracion = (EditText)findViewById(R.id.duracion);

        spinner = (Spinner) findViewById(R.id.spinnerCategoria);

        introListaSpinner(spinner,listaCategorias);

        spinner.setSelection(listaCategorias.size());

        if(peliculaSeleccionada != null)
            abrirModoConsulta(peliculaSeleccionada);

        FloatingActionButton botonGuardar = findViewById(R.id.botonGuardar);

        botonGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                Snackbar.make(findViewById(R.id.layoutPrincipal),R.string.msg_guardando,Snackbar.LENGTH_LONG).show();
                addPelicula();
            }
        });

        ImageButton botonCategoria = findViewById(R.id.botonEditar);
        botonCategoria.setEnabled(false);
        botonCategoria.setVisibility(View.GONE);
        botonCategoria.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(final View v){
               Spinner spinner = findViewById(R.id.spinnerCategoria);
               if(spinner.getSelectedItemPosition()==0){
                   msgCreaCategoria = Snackbar.make(findViewById(R.id.layoutPrincipal),R.string.msg_crea_categoria,Snackbar.LENGTH_LONG);
               }else{
                   msgCreaCategoria = Snackbar.make(findViewById(R.id.layoutPrincipal),R.string.msg_modif_categoria,Snackbar.LENGTH_LONG);
               }
               msgCreaCategoria.setAction(android.R.string.cancel,new View.OnClickListener(){
                   @Override
                   public void onClick(View v){
                        Snackbar.make(findViewById(R.id.layoutPrincipal),R.string.msg_accion_cancelada,Snackbar.LENGTH_LONG).show();
                   }
               });

               msgCreaCategoria.setAction(android.R.string.ok,new View.OnClickListener(){
                   @Override
                   public void onClick(View v){
                       Snackbar.make(findViewById(R.id.layoutPrincipal),R.string.msg_accion_ok,Snackbar.LENGTH_LONG).show();
                       modificarCategoria();
                   }
               });

               msgCreaCategoria.show();
           }
        });
    }

    private void addPelicula(){
        Categoria cat = listaCategorias.get(spinner.getSelectedItemPosition()-1);
        Pelicula pelicula = new Pelicula(titulo.getText().toString(),argumento.getText().toString(),cat,duracion.getText().toString(),"",
                                            MainRecycler.caratula_por_defecto,MainRecycler.fondo_por_defecto,MainRecycler.trailer_por_defecto);
        Intent intentResult = new Intent();
        intentResult.putExtra(MainRecycler.PELICULA_ADD,pelicula);
        setResult(RESULT_OK,intentResult);
        finish();
    }

    private void abrirModoConsulta(Pelicula peli){
        titulo.setText(peli.getTitulo());
        argumento.setText(peli.getArgumento());
        duracion.setText(peli.getDuracion());

        String nombre = peli.getCategoria().getNombre();
        int posicion = 0;
        int i = 1;

        for(Categoria categoria : listaCategorias) {
            posicion = categoria.getNombre().equals(nombre) ? i : 0;
            i++;
        }
        spinner.setSelection(posicion);

        spinner.setEnabled(false);
        titulo.setEnabled(false);
        argumento.setEnabled(false);
        duracion.setEnabled(false);
    }

    private void introListaSpinner(Spinner spinner,List<Categoria> listaCategorias){
        List<String> nombres = new ArrayList<String>();
        nombres.add("Sin definir");
        for(Categoria c : listaCategorias)
            nombres.add(c.getNombre());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void modificarCategoria(){
        Intent categoriaIntent = new Intent(MainActivity.this,CategoriaActivity.class);

        categoriaIntent.putExtra(POS_CATEGORIA_SELECCIONADA,spinner.getSelectedItemPosition());
        creandoCategoria = true;
        if(spinner.getSelectedItemPosition()>0){
            creandoCategoria = false;
            categoriaIntent.putExtra(CATEGORIA_SELECCIONADA,listaCategorias.get(spinner.getSelectedItemPosition() - 1));
        }

        startActivityForResult(categoriaIntent,GESTION_CATEGORIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GESTION_CATEGORIA){
            if(resultCode == RESULT_OK){
                Categoria categoria = data.getParcelableExtra(CATEGORIA_MODIFICADA);
                if(creandoCategoria){
                    listaCategorias.add(categoria);
                    introListaSpinner(spinner,listaCategorias);
                }else{
                    for(Categoria cat: listaCategorias){
                        if(cat.getNombre().equals(categoria.getNombre())){
                            cat.setDescripcion(categoria.getDescripcion());
                            Log.d("FavMovie.MainActivity","Modificada descripción de: "+cat.getNombre());
                            break;
                        }
                    }
                }
            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("FavMovie.MainActivity","CategoriaActivity cancelada");
    }
}