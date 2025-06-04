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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private Spinner spinnerFiltro;
    private Button btnAgregar, btnGrupos;
    private AdaptadorTareas adaptador;
    private ArrayList<Tareas> listaTareas;
    private DBTareas dbHelper;
    private Tareas selectedTarea;

    private static final String CHANNEL_ID = "canal_tareas";

    private ArrayList<String> listaOpcionesFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerTareas = findViewById(R.id.recyclerTareas);
        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnGrupos = findViewById(R.id.btnGrupos);

        dbHelper = new DBTareas(this);
        listaTareas = new ArrayList<>();
        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new AdaptadorTareas(this, listaTareas,
                tarea -> selectedTarea = tarea,
                this::mostrarMenuFlotante
        );
        recyclerTareas.setAdapter(adaptador);

        cargarOpcionesFiltro();

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                aplicarFiltro(pos);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        crearCanalNotificacion();
        solicitarPermisoNotificaciones();

        btnAgregar.setOnClickListener(v -> {
            mostrarNotificacion("Tarea nueva", "Abriendo pantalla para agregar tarea");
            startActivity(new Intent(MainActivity.this, AgregarTarea.class));
        });

        btnGrupos.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Lista_Grupos.class))
        );
    }

    private void cargarOpcionesFiltro() {
        listaOpcionesFiltro = new ArrayList<>();
        listaOpcionesFiltro.add("Pendientes");
        listaOpcionesFiltro.add("Realizadas");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaOpcionesFiltro);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapter);
    }


    private void aplicarFiltro(int pos) {
        // Como solo hay 2 opciones, pos 0 = Pendientes (realizada = 0), pos 1 = Realizadas (realizada = 1)
        cargarTareas(pos, null);
    }


    private void cargarTareas(int filtroRealizada, String grupoFiltroIgnorado) {
        listaTareas.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DBTareas.TABLA_TAREAS + " WHERE realizada = ?";
        String[] args = new String[]{String.valueOf(filtroRealizada)};

        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                Tareas tarea = new Tareas();
                tarea.setId(cursor.getInt(0));
                tarea.setTitulo(cursor.getString(1));
                tarea.setDescripcion(cursor.getString(2));
                tarea.setGrupo(cursor.getString(3));
                tarea.setFechaLimite(cursor.getString(4));
                tarea.setRealizada(cursor.getInt(5) == 1);
                tarea.setHoraRecordatorio(cursor.getString(6));
                tarea.setRepetirDiariamente(cursor.getInt(7) == 1);
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
        selectedTarea = null;
    }


    private void mostrarMenuFlotante(View vista, Tareas tarea) {
        selectedTarea = tarea;
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this, vista);
        popup.getMenuInflater().inflate(R.menu.menu_tarea_contextual, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.mnxModificar) {
                Intent i = new Intent(MainActivity.this, AgregarTarea.class);
                i.putExtra("id", tarea.getId());
                startActivity(i);
                return true;
            } else if (item.getItemId() == R.id.mnxEliminar) {
                showDeleteConfirmationDialog(tarea);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showDeleteConfirmationDialog(Tareas tarea) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Tarea")
                .setMessage("¿Eliminar la tarea seleccionada?")
                .setPositiveButton("Eliminar", (d, w) -> deleteTarea(tarea))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteTarea(Tareas tarea) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBTareas.TABLA_TAREAS, "id = ?", new String[]{String.valueOf(tarea.getId())});
        aplicarFiltro(spinnerFiltro.getSelectedItemPosition());
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Canal de Tareas", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private void solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void mostrarNotificacion(String titulo, String mensaje) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(this)
                .notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override protected void onResume() {
        super.onResume();
        aplicarFiltro(spinnerFiltro.getSelectedItemPosition());
    }

    // Este menú de opciones solo se requiere si tienes más acciones en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Opcional: podrías limpiar este bloque si ya no usas opciones como "Modificar", etc.
        return super.onOptionsItemSelected(item);
    }
}
