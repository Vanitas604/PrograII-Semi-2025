package com.example.proyectofinal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;

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

    // ===== MÉTODOS DEL MENÚ =====
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Asegúrate de que se llame menu.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnxNuevo) {
            startActivity(new Intent(this, AgregarTarea.class));
            return true;
        } else if (id == R.id.mnxModificar) {
            Toast.makeText(this, "Modificar tarea (aún no implementado)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.mnxEliminar) {
            Toast.makeText(this, "Eliminar tarea (aún no implementado)", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


