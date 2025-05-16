package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AgregarTarea extends AppCompatActivity {

    private EditText txtTarea, txtDescripcion;
    private Spinner spinnerGrupos;
    private Button btnGuardar, btnCancelar;
    private DBTareas dbHelper;
    private List<Grupo> listaGrupos;
    private ArrayAdapter<Grupo> adaptadorGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        txtTarea = findViewById(R.id.edtTitulo);
        txtDescripcion = findViewById(R.id.edtDescripcion);
        spinnerGrupos = findViewById(R.id.spinnerGrupo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar); // Asegúrate de que exista en tu XML

        dbHelper = new DBTareas(this);

        cargarGruposEnSpinner();

        btnGuardar.setOnClickListener(view -> guardarTarea());

        btnCancelar.setOnClickListener(view -> finish());
    }

    private void cargarGruposEnSpinner() {
        listaGrupos = dbHelper.obtenerGrupos();

        adaptadorGrupos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaGrupos);
        adaptadorGrupos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupos.setAdapter(adaptadorGrupos);
    }

    private void guardarTarea() {
        String nombreTarea = txtTarea.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();

        if (nombreTarea.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaGrupos == null || listaGrupos.isEmpty()) {
            Toast.makeText(this, "No hay grupos disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        int posicionGrupo = spinnerGrupos.getSelectedItemPosition();
        int grupoId = listaGrupos.get(posicionGrupo).getId();

        long resultado = dbHelper.insertarTarea(nombreTarea, descripcion, grupoId);

        if (resultado != -1) {
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}






