package com.example.proyectofinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaTareasPorGrupo extends AppCompatActivity {

    RecyclerView recyclerView;
    AdaptadorTareas adaptador;
    DBTareas dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tareas_por_grupo);

        recyclerView = findViewById(R.id.recyclerTareasGrupo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBTareas(this);

        String nombreGrupo = getIntent().getStringExtra("nombreGrupo");

        // Obtener tareas por grupo desde SQLite
        List<Tareas> listaTareas = dbHelper.obtenerTareasPorGrupo(nombreGrupo);

        adaptador = new AdaptadorTareas(this, listaTareas, t -> {}, (v, t) -> {});
        recyclerView.setAdapter(adaptador);
    }
}

