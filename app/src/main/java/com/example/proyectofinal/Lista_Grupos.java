package com.example.proyectofinal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Lista_Grupos extends AppCompatActivity {

    ListView listaGrupos;
    Button btnAgregarGrupo;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaNombres;
    ArrayList<Grupo> listaObjetos;

    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);

        listaGrupos = findViewById(R.id.listaGrupos);
        btnAgregarGrupo = findViewById(R.id.btnAgregarGrupo);

        dbHelper = new DBTareas(this);

        cargarGrupos();

        btnAgregarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoAgregar();
            }
        });
    }

    private void cargarGrupos() {
        listaNombres = new ArrayList<>();
        listaObjetos = new ArrayList<>();

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DBTareas.TABLA_GRUPO, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);

                listaObjetos.add(new Grupo(id, nombre));
                listaNombres.add(nombre);
            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNombres);
        listaGrupos.setAdapter(adapter);
    }

    private void mostrarDialogoAgregar() {
        final EditText input = new EditText(this);
        input.setHint("Nombre del grupo");

        new android.app.AlertDialog.Builder(this)
                .setTitle("Nuevo Grupo")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = input.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        dbHelper.getWritableDatabase().execSQL("INSERT INTO " + DBTareas.TABLA_GRUPO + " (nombre) VALUES (?)", new Object[]{nombre});
                        cargarGrupos(); // Recargar lista
                    } else {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

