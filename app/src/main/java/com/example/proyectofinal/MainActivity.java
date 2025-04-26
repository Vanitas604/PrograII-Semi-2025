package com.example.proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerTareas;
    AdaptadorTareas adaptador;
    ArrayList<Tareas> listaTareas;
    Button btnAgregar, btnGrupos, btnCuenta;

    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Lo haremos con RelativeLayout

        recyclerTareas = findViewById(R.id.recyclerTareas);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnGrupos = findViewById(R.id.btnGrupos);
        btnCuenta = findViewById(R.id.btnCuenta);

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();

        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorTareas(this, listaTareas);
        recyclerTareas.setAdapter(adaptador);

        cargarTareas();

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearTareaActivity.class);
            startActivity(intent);
        });

        btnGrupos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GruposActivity.class);
            startActivity(intent);
        });

        btnCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CuentaActivity.class);
            startActivity(intent);
        });
    }

    private void cargarTareas() {
        listaTareas.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBTareas.TABLA_TAREAS, null);

        if (cursor.moveToFirst()) {
            do {
                Tareas tarea = new Tareas();
                tarea.setId(cursor.getInt(0));
                tarea.setTitulo(cursor.getString(1));
                tarea.setDescripcion(cursor.getString(2));
                tarea.setGrupo(cursor.getString(3));
                tarea.setFechaLimite(cursor.getString(4));
                tarea.setRealizada(cursor.getInt(5) == 1);
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }

        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}