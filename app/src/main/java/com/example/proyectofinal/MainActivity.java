package com.example.proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerTareas;
    AdaptadorTareas adaptador;
    ArrayList<Tareas> listaTareas;
    Button btnAgregar, btnGrupos;

    DBTareas dbHelper;
    Tareas selectedTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerTareas = findViewById(R.id.recyclerTareas);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnGrupos = findViewById(R.id.btnGrupos);

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();

        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorTareas(this, listaTareas, tarea -> {
            selectedTarea = tarea;
            Toast.makeText(this, "Seleccionado: " + tarea.getTitulo(), Toast.LENGTH_SHORT).show();
        }, this::mostrarMenuFlotante); // Pasamos el método para long click
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
        selectedTarea = null;
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
        } else if (id == R.id.mnxModificar) {
            if (selectedTarea != null) {
                Intent intent = new Intent(this, AgregarTarea.class);
                intent.putExtra("id", selectedTarea.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selecciona una tarea para modificar", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.mnxEliminar) {
            if (selectedTarea != null) {
                showDeleteConfirmationDialog(selectedTarea);
            } else {
                Toast.makeText(this, "Selecciona una tarea para eliminar", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog(Tareas tarea) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteTarea(tarea))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteTarea(Tareas tarea) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBTareas.TABLA_TAREAS, "id = ?", new String[]{String.valueOf(tarea.getId())});
        cargarTareas();
    }

    // Método para mostrar el menú flotante cuando se hace un long click en una tarea
    private void mostrarMenuFlotante(View vista, Tareas tarea) {
        selectedTarea = tarea;

        // Crear el PopupMenu y asociarlo a la vista (el item de tarea)
        PopupMenu popup = new PopupMenu(this, vista);

        // Infla el menú contextual con el archivo menu_tarea_contextual.xml
        popup.getMenuInflater().inflate(R.menu.menu_tarea_contextual, popup.getMenu());

        // Configura el comportamiento de las opciones del menú
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mnxModificar) {
                // Si se selecciona "Modificar", se abre la pantalla para editar la tarea
                Intent intent = new Intent(MainActivity.this, AgregarTarea.class);
                intent.putExtra("id", tarea.getId());
                startActivity(intent);
                return true;
            } else if (id == R.id.mnxEliminar) {
                // Si se selecciona "Eliminar", muestra el diálogo de confirmación
                showDeleteConfirmationDialog(tarea);
                return true;
            }
            return false;
        });

        // Mostrar el menú emergente
        popup.show();
    }
}





