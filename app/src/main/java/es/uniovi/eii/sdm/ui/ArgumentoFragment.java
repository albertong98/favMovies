package es.uniovi.eii.sdm.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.uniovi.eii.sdm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArgumentoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArgumentoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGUMENTO = "argumento";

    // TODO: Rename and change types of parameters
    private String argumento;

    public ArgumentoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ArgumentoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArgumentoFragment newInstance(String argumento) {
        ArgumentoFragment fragment = new ArgumentoFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENTO,argumento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            argumento = getArguments().getString(ARGUMENTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_argumento, container, false);
        TextView targumento = root.findViewById(R.id.argumentoFragment);
        targumento.setText(argumento);

        return root;
    }
}