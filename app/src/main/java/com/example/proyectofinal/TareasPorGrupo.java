package com.example.proyectofinal;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TareasPorGrupo extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private AdaptadorTareas adaptador;
    private DBTareas dbHelper;
    private ArrayList<Tareas> listaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tareas_por_grupo);

        recyclerTareas = findViewById(R.id.recyclerTareasGrupo);
        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();

        String nombreGrupo = getIntent().getStringExtra("nombreGrupo");

        if (nombreGrupo == null || nombreGrupo.isEmpty()) {
            Toast.makeText(this, "Error: No se recibió el nombre del grupo", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Toast.makeText(this, "Grupo recibido: " + nombreGrupo, Toast.LENGTH_SHORT).show();

        cargarTareasPorGrupo(nombreGrupo);

        adaptador = new AdaptadorTareas(this, listaTareas, tarea -> {
            // clic
        }, (v, tarea) -> {
            // long clic
        });

        recyclerTareas.setAdapter(adaptador);
        adaptador.notifyDataSetChanged(); // Asegura que el RecyclerView se actualice
    }

    private void cargarTareasPorGrupo(String grupo) {
        listaTareas.clear();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT id, titulo, descripcion, grupo, fecha_limite, realizada FROM " + DBTareas.TABLA_TAREAS +
                        " WHERE grupo = ?", new String[]{grupo});

        if (cursor.moveToFirst()) {
            do {
                Tareas tarea = new Tareas(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5) == 1
                );
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
