package es.uniovi.eii.sdm.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.Actor;

public class ActoresDataSource {
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_REPARTO,MyDBHelper.COLUMNA_NOMBRE_ACTOR,
                                         MyDBHelper.COLUMNA_IMAGEN_ACTOR,MyDBHelper.COLUMNA_URL_imdb};

    public ActoresDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createactor(Actor actorToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_REPARTO,actorToInsert.getId());
        values.put(MyDBHelper.COLUMNA_NOMBRE_ACTOR,actorToInsert.getNombre());
        values.put(MyDBHelper.COLUMNA_IMAGEN_ACTOR,actorToInsert.getImagen());
        values.put(MyDBHelper.COLUMNA_URL_imdb,actorToInsert.getURL_imdb());

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_PELICULAS, null, values);

        return insertId;
    }

    public List<Actor> getAllValorations() {
        // Lista que almacenara el resultado
        List<Actor> actorList = new ArrayList<Actor>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_PELICULAS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Actor actor= new Actor();
            cursor.getInt(0);
            actor.setId(cursor.getInt(0));
            actor.setNombre(cursor.getString(1));
            actor.setImagen("https://image.tmdb.org/t/p/original/"+cursor.getString(2));
            actor.setURL_imdb("https://www.imdb.com/name/"+cursor.getString(3));
            actorList.add(actor);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return actorList;
    }
}
