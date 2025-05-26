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

        String[] columnas = {
                DBTareas.COLUMNA_ID,
                DBTareas.COLUMNA_TITULO,
                DBTareas.COLUMNA_DESCRIPCION,
                DBTareas.COLUMNA_GRUPO,
                DBTareas.COLUMNA_FECHA_LIMITE,
                DBTareas.COLUMNA_HORA_RECORDATORIO,
                DBTareas.COLUMNA_REPETIR_DIARIAMENTE,
                DBTareas.COLUMNA_REALIZADA
        };

        Cursor cursor = dbHelper.getReadableDatabase().query(
                DBTareas.TABLA_TAREAS,
                columnas,
                DBTareas.COLUMNA_GRUPO + " = ?",
                new String[]{grupo},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Tareas tarea = new Tareas();
                tarea.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_ID)));
                tarea.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_TITULO)));
                tarea.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_DESCRIPCION)));
                tarea.setGrupo(cursor.getString(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_GRUPO)));
                tarea.setFechaLimite(cursor.getString(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_FECHA_LIMITE)));
                tarea.setHoraRecordatorio(cursor.getString(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_HORA_RECORDATORIO)));
                tarea.setRepetirDiariamente(cursor.getInt(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_REPETIR_DIARIAMENTE)) == 1);
                tarea.setRealizada(cursor.getInt(cursor.getColumnIndexOrThrow(DBTareas.COLUMNA_REALIZADA)) == 1);

                listaTareas.add(tarea);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}
