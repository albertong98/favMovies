package es.uniovi.eii.sdm.datos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.PeliculaReparto;

public class PeliculaRepartoDataSource {
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_REPARTO,MyDBHelper.COLUMNA_ID_PELICULAS,MyDBHelper.COLUMNA_PERSONAJE};

    public PeliculaRepartoDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createpeliculareparto(PeliculaReparto peliculaRepartoToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_REPARTO,peliculaRepartoToInsert.getId_actor());
        values.put(MyDBHelper.COLUMNA_ID_PELICULAS,peliculaRepartoToInsert.getId_pelicula());
        values.put(MyDBHelper.COLUMNA_PERSONAJE,peliculaRepartoToInsert.getNombre_personaje());

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_PELICULAS_REPARTO, null, values);

        return insertId;
    }

    public List<PeliculaReparto> getAllValorations() {
        // Lista que almacenara el resultado
        List<PeliculaReparto> peliculaRepartoList = new ArrayList<PeliculaReparto>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_PELICULAS_REPARTO, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final PeliculaReparto peliculaReparto = new PeliculaReparto();
            peliculaReparto.setId_actor(cursor.getInt(0));
            peliculaReparto.setId_pelicula(cursor.getInt(1));
            peliculaReparto.setNombre_personaje(cursor.getString(2));
            peliculaRepartoList.add(peliculaReparto);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return peliculaRepartoList;
    }

}
