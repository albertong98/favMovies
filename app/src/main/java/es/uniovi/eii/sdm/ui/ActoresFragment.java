package es.uniovi.eii.sdm.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.uniovi.eii.sdm.ListaActoresAdapter;
import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.datos.ActoresDataSource;
import es.uniovi.eii.sdm.modelo.Actor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActoresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_PELICULA = "id_pelicula";


    // TODO: Rename and change types of parameters
    private int idPelicula;
    private List<Actor> listaActores;
    private RecyclerView listaActoresView;
    public ActoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ActoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActoresFragment newInstance(int id) {
        ActoresFragment fragment = new ActoresFragment();
        Bundle args = new Bundle();
        args.putInt(ID_PELICULA, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPelicula = getArguments().getInt(ID_PELICULA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_actores, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        listaActoresView = root.findViewById(R.id.recyclerActores);
        listaActoresView.setLayoutManager(layoutManager);
        listaActoresView.setHasFixedSize(true);
        ActoresDataSource actoresDataSource = new ActoresDataSource(root.getContext());
        actoresDataSource.open();
        listaActores = actoresDataSource.actoresParticipantes(idPelicula);
        actoresDataSource.close();
        Log.i("N ACTORES","n= "+listaActores.size());
        // Inflate the layout for this fragment
        ListaActoresAdapter laAdapter = new ListaActoresAdapter(listaActores,
                new ListaActoresAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Actor item) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getURL_imdb())));
                    }
                });
        listaActoresView.setAdapter(laAdapter);
        return root;
    }
}