package com.example.proyectofinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DetalleTareas extends AppCompatActivity {

    private ListView listViewPendientes;
    private DBTareas dbTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tareas);

        listViewPendientes = findViewById(R.id.listViewPendientes);
        dbTareas = new DBTareas(this);

        cargarTareasPendientes();
    }

    private void cargarTareasPendientes() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = dbTareas.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT titulo, grupo FROM tareas WHERE realizada = 0", null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                String grupo = cursor.getString(1);
                lista.add("🔹 " + titulo + " (Grupo: " + grupo + ")");
            } while (cursor.moveToNext());
        } else {
            lista.add("No hay tareas pendientes");
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewPendientes.setAdapter(adapter);
    }
}

