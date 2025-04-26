package com.example.proyectofinal;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarTarea extends AppCompatActivity {

    EditText edtTitulo, edtDescripcion, edtGrupo, edtFecha;
    CheckBox chkRealizada;
    Button btnGuardar, btnCancelar;

    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea); // usando tu layout

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtGrupo = findViewById(R.id.edtGrupo);
        edtFecha = findViewById(R.id.edtFecha);
        chkRealizada = findViewById(R.id.chkRealizada);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBTareas(this);

        btnGuardar.setOnClickListener(v -> guardarTarea());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarTarea() {
        String titulo = edtTitulo.getText().toString();
        String descripcion = edtDescripcion.getText().toString();
        String grupo = edtGrupo.getText().toString();
        String fecha = edtFecha.getText().toString();
        boolean realizada = chkRealizada.isChecked();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("descripcion", descripcion);
        valores.put("grupo", grupo);
        valores.put("fecha_limite", fecha);
        valores.put("realizada", realizada ? 1 : 0);

        long id = dbHelper.getWritableDatabase().insert(DBTareas.TABLA_TAREAS, null, valores);

        if (id > 0) {
            Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}
