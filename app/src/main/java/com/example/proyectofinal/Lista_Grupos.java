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

    private RecyclerView recyclerGrupos;
    private AdaptadorGrupos adaptador;
    private ArrayList<Grupo> listaGrupos;
    private DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);

        // Inicialización de vistas
        recyclerGrupos = findViewById(R.id.recyclerGrupos);
        dbHelper = new DBTareas(this);

        // Configuración del RecyclerView
        listaGrupos = new ArrayList<>();
        recyclerGrupos.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new AdaptadorGrupos(this, listaGrupos, new AdaptadorGrupos.OnGrupoClickListener() {
            @Override
            public void onClick(Grupo grupo) {
                // NUEVO: ir a la actividad que muestra las tareas del grupo
                Intent intent = new Intent(Lista_Grupos.this, TareasPorGrupo.class);
                intent.putExtra("nombreGrupo", grupo.getNombre()); // pasamos el nombre del grupo
                startActivity(intent);
            }

            @Override
            public void onModificar(Grupo grupo) {
                mostrarDialogoEditar(grupo);
            }

            @Override
            public void onEliminar(Grupo grupo) {
                eliminarGrupo(grupo);
            }
        });

        recyclerGrupos.setAdapter(adaptador);

        // Cargar datos iniciales
        cargarGrupos();

        // Listeners
        findViewById(R.id.btnAgregarGrupo).setOnClickListener(view -> mostrarDialogoAgregar());
    }





    private void cargarGrupos() {
        listaGrupos.clear();
        try (Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT id, nombre FROM " + DBTareas.TABLA_GRUPO, null)) {

            if (cursor.moveToFirst()) {
                do {
                    listaGrupos.add(new Grupo(
                            cursor.getInt(0),    // id
                            cursor.getString(1) // nombre
                    ));
                } while (cursor.moveToNext());
            }
        }
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
                        Toast.makeText(this, "Grupo creado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditar(Grupo grupo) {
        final EditText input = new EditText(this);
        input.setText(grupo.getNombre());

        new android.app.AlertDialog.Builder(this)
                .setTitle("Editar Grupo")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoNombre = input.getText().toString().trim();
                    if (!nuevoNombre.isEmpty()) {
                        dbHelper.getWritableDatabase().execSQL(
                                "UPDATE " + DBTareas.TABLA_GRUPO + " SET nombre = ? WHERE id = ?",
                                new Object[]{nuevoNombre, grupo.getId()});
                        cargarGrupos();
                        Toast.makeText(this, "Grupo actualizado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarGrupo(Grupo grupo) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Grupo")
                .setMessage("¿Estás seguro de eliminar el grupo " + grupo.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    dbHelper.getWritableDatabase().execSQL(
                            "DELETE FROM " + DBTareas.TABLA_GRUPO + " WHERE id = ?",
                            new Object[]{grupo.getId()});
                    cargarGrupos();
                    Toast.makeText(this, "Grupo eliminado", Toast.LENGTH_SHORT).show();
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
        if (item.getItemId() == R.id.mnxNuevo) {
            startActivity(new Intent(this, AgregarTarea.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}



