package com.example.proyectofinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TareasRealizadas extends AppCompatActivity {

    private ListView listViewTareasRealizadas;
    private DBTareas dbTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_realizadas);

        listViewTareasRealizadas = findViewById(R.id.listViewTareasRealizadas);
        dbTareas = new DBTareas(this);

        cargarTareasRealizadas();
    }

    private void cargarTareasRealizadas() {
        ArrayList<String> tareasRealizadas = new ArrayList<>();

        SQLiteDatabase db = dbTareas.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT titulo, descripcion, fecha_limite FROM tareas WHERE realizada = 1", null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                String descripcion = cursor.getString(1);
                String fecha = cursor.getString(2);
                tareasRealizadas.add("Título: " + titulo + "\nDescripción: " + descripcion + "\nFecha: " + fecha);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareasRealizadas);
        listViewTareasRealizadas.setAdapter(adapter);
    }
}

