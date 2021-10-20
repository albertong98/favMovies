package es.uniovi.eii.sdm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conexion {
    private Context nContexto;

    public Conexion(Context nContexto){ this.nContexto = nContexto; }

    public boolean CompruebaConexion(){
        boolean conectado = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager)nContexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        conectado = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return conectado;
    }
}
