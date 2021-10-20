package es.uniovi.eii.sdm.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.uniovi.eii.sdm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CARATULA ="caratula";
    private static final String FECHA = "fecha";
    private static final String DURACION = "duracion";

    // TODO: Rename and change types of parameters
    private String fecha;
    private String duracion;
    private String caratula;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String fecha,String caratula,String duracion) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(FECHA,fecha);
        args.putString(CARATULA, caratula);
        args.putString(DURACION,duracion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fecha = getArguments().getString(FECHA);
            caratula = getArguments().getString(CARATULA);
            duracion = getArguments().getString(DURACION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        TextView testreno = root.findViewById(R.id.estrenofragment);
        TextView tduracion = root.findViewById(R.id.duracionPelicula);
        ImageView caratula = root.findViewById(R.id.caratula);

        testreno.setText(fecha);
        tduracion.setText(duracion);
        Picasso.get().load(this.caratula).into(caratula);

        return root;
    }
}