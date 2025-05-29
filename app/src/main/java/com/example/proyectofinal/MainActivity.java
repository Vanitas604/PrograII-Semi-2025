package com.example.proyectofinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    private static final String CHANNEL_ID = "canal_tareas";

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
        }, this::mostrarMenuFlotante);
        recyclerTareas.setAdapter(adaptador);

        cargarTareas();

        // Crear canal de notificación (Android 8 +)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Tareas",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Pedir permiso de notificaciones (Android 13 +)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        btnAgregar.setOnClickListener(v -> {
            mostrarNotificacion("Tarea nueva", "Abriendo pantalla para agregar tarea");
            startActivity(new Intent(MainActivity.this, AgregarTarea.class));
        });

        btnGrupos.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Lista_Grupos.class));
        });
    }

    /** Carga todas las tareas, incluyendo hora_recordatorio y repetir_diariamente */
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
                tarea.setHoraRecordatorio(cursor.getString(6));          // ← NUEVO
                tarea.setRepetirDiariamente(cursor.getInt(7) == 1);      // ← NUEVO
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
        } else if (id == R.id.mnxAjustes) {
            startActivity(new Intent(this, AjustesActivity.class));
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

    private void mostrarMenuFlotante(View vista, Tareas tarea) {
        selectedTarea = tarea;

        PopupMenu popup = new PopupMenu(this, vista);
        popup.getMenuInflater().inflate(R.menu.menu_tarea_contextual, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mnxModificar) {
                Intent intent = new Intent(MainActivity.this, AgregarTarea.class);
                intent.putExtra("id", tarea.getId());
                startActivity(intent);
                return true;
            } else if (id == R.id.mnxEliminar) {
                showDeleteConfirmationDialog(tarea);
                return true;
            }
            return false;
        });

        popup.show();
    }

    /** Notificación rápida para comprobar que el canal funciona */
    @SuppressLint("MissingPermission")
    private void mostrarNotificacion(String titulo, String mensaje) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif) // asegúrate de tener este icono
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(this)
                .notify((int) System.currentTimeMillis(), builder.build());
    }
}
