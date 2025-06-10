package com.example.proyectofinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaContactosActivity extends AppCompatActivity {

    ListView listViewContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contacto);

        listViewContactos = findViewById(R.id.listViewContactos);

        ArrayList<String> lista = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null);

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(1);
            String apellido = cursor.getString(2);
            String tipo = cursor.getString(5);
            lista.add(nombre + " " + apellido + " (" + tipo + ")");
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewContactos.setAdapter(adapter);
    }
}