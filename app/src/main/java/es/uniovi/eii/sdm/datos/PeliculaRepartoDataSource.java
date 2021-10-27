package es.uniovi.eii.sdm.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import es.uniovi.eii.sdm.modelo.Actor;

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



}
