package com.example.proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerTareas;
    AdaptadorTareas adaptador;
    ArrayList<Tareas> listaTareas;
    Button btnAgregar, btnGrupos;

    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Lo haremos con RelativeLayout

        recyclerTareas = findViewById(R.id.recyclerTareas);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnGrupos = findViewById(R.id.btnGrupos);

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();

        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorTareas(this, listaTareas);
        recyclerTareas.setAdapter(adaptador);

        cargarTareas();

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarTarea.class);
            startActivity(intent);
        });

        btnGrupos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Lista_Grupos.class);
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

    // Inflar el menú (usa tu archivo res/menu/menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // ← Asegúrate que se llame menu.xml
        return true;
    }

    // Manejar las acciones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnxNuevo) {
            Intent intent = new Intent(this, AgregarTarea.class);
            startActivity(intent);
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

