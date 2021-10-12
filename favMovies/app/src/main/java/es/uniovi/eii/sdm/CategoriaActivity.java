package es.uniovi.eii.sdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.uniovi.eii.sdm.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        Intent intent = getIntent();
        int posCategoria = intent.getIntExtra(MainActivity.POS_CATEGORIA_SELECCIONADA,0);
        Categoria categEntrada = null;

        if(posCategoria > 0)
            categEntrada = intent.getParcelableExtra(MainActivity.CATEGORIA_SELECCIONADA);

        TextView textViewCreacion = findViewById(R.id.creacion);

        EditText editNombre = findViewById(R.id.editnombre);
        EditText editDescripcion = findViewById(R.id.editdescripcion);

        Button buttonOk = findViewById(R.id.buttonOk);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        if(posCategoria > 0) {
            editNombre.setText(categEntrada.getNombre());
            editDescripcion.setText(categEntrada.getDescripcion());
            editNombre.setEnabled(false);
            textViewCreacion.setText(R.string.msg_modif_categoria);
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categoria categoria = new Categoria(editNombre.getText().toString(),editDescripcion.getText().toString());
                Intent intentResult = new Intent();
                intentResult.putExtra(MainActivity.CATEGORIA_MODIFICADA,categoria);
                setResult(RESULT_OK,intentResult);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }


}