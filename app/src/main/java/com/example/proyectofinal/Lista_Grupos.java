package com.example.proyectofinal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Lista_Grupos extends AppCompatActivity {

    RecyclerView recyclerGrupos;
    AdaptadorGrupos adaptador;
    ArrayList<Grupo> listaGrupos;
    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);

        recyclerGrupos = findViewById(R.id.recyclerGrupos);
        dbHelper = new DBTareas(this);

        listaGrupos = new ArrayList<>();
        recyclerGrupos.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorGrupos(listaGrupos, grupo -> {
            Toast.makeText(this, "Seleccionado: " + grupo.getNombre(), Toast.LENGTH_SHORT).show();
        });
        recyclerGrupos.setAdapter(adaptador);

        cargarGrupos();

        findViewById(R.id.btnAgregarGrupo).setOnClickListener(view -> mostrarDialogoAgregar());
    }

    private void cargarGrupos() {
        listaGrupos.clear();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DBTareas.TABLA_GRUPO, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                listaGrupos.add(new Grupo(id, nombre));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
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
                        dbHelper.getWritableDatabase().execSQL(
                                "INSERT INTO " + DBTareas.TABLA_GRUPO + " (nombre) VALUES (?)",
                                new Object[]{nombre});
                        cargarGrupos();
                    } else {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnxNuevo) {
            startActivity(new Intent(this, AgregarTarea.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



