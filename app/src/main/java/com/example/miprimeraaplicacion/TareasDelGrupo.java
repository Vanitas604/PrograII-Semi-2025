package com.example.miprimeraaplicacion;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TareasDelGrupo extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private AdaptadorTareas adaptador;
    private ArrayList<Tareas> listaTareas;
    private DBTareas dbHelper;
    private TextView txtTituloGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_del_grupo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTituloGrupo = findViewById(R.id.txtTituloGrupo);
        recyclerTareas = findViewById(R.id.recyclerTareas);

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();
        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new AdaptadorTareas(this, listaTareas,
                tarea -> {
                    // Manejo de clic normal (si se desea)
                },
                (vista, tarea) -> {
                    // Manejo de menú contextual (si se desea)
                });

        recyclerTareas.setAdapter(adaptador);

        // Obtener datos del Intent
        int idGrupo = getIntent().getIntExtra("idGrupo", -1);
        String nombreGrupo = getIntent().getStringExtra("nombreGrupo");
        txtTituloGrupo.setText("Tareas del grupo: " + nombreGrupo);

        if (idGrupo != -1) {
            cargarTareasDelGrupo(idGrupo, nombreGrupo);
        } else {
            Toast.makeText(this, "No se encontró el grupo", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarTareasDelGrupo(int idGrupo, String nombreGrupo) {
        listaTareas.clear();

        String sql = "SELECT id, titulo, descripcion, fecha_limite, realizada FROM " + DBTareas.TABLA_TAREAS + " WHERE grupo = ?";
        try (Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(idGrupo)})) {
            if (cursor.moveToFirst()) {
                do {
                    listaTareas.add(new Tareas(
                            cursor.getInt(0),       // id
                            cursor.getString(1),    // titulo
                            cursor.getString(2),    // descripcion
                            nombreGrupo,            // nombre del grupo
                            cursor.getString(3),    // fecha_limite
                            cursor.getInt(4) == 1   // realizada
                    ));
                } while (cursor.moveToNext());
            }
        }

        adaptador.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}





