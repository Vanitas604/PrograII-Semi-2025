package com.example.proyectofinal;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TareasPendientes extends AppCompatActivity {

    private ListView listViewPendientes;
    private DBTareas dbTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tareas); // Asegúrate de que así se llama tu XML

        listViewPendientes = findViewById(R.id.listViewPendientes);
        dbTareas = new DBTareas(this);

        cargarTareasPendientes();
    }

    private void cargarTareasPendientes() {
        ArrayList<String> listaPendientes = new ArrayList<>();
        SQLiteDatabase db = dbTareas.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT titulo, descripcion, fecha_limite FROM tareas WHERE realizada = 0", null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                String descripcion = cursor.getString(1);
                String fecha = cursor.getString(2);
                listaPendientes.add("Título: " + titulo + "\nDescripción: " + descripcion + "\nFecha límite: " + fecha);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaPendientes);
        listViewPendientes.setAdapter(adapter);
    }
}
