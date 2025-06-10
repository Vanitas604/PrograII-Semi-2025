package com.example.proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaContactosActivity extends AppCompatActivity {

    ListView listViewContactos;
    ArrayList<String> lista;
    ArrayList<String> nombresCompletos; // Lista paralela para guardar los nombres completos reales

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contacto);

        listViewContactos = findViewById(R.id.listViewContactos);

        lista = new ArrayList<>();
        nombresCompletos = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null);

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(1);
            String apellido = cursor.getString(2);
            String correo = cursor.getString(3);
            String apodo = cursor.getString(4);
            // Asumimos que el tipo es una cadena, puede ser "Privada" o "Persona"

            String tipo = cursor.getString(5);

            String nombreCompleto = nombre + " " + apellido;
            String mostrarEnLista = nombreCompleto + " (" + tipo + ")";

            lista.add(mostrarEnLista);
            nombresCompletos.add(nombreCompleto); // Guardamos solo el nombre para pasar a Chats
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewContactos.setAdapter(adapter);

        // Agregamos el clic para ir al chat
        listViewContactos.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String nombreSeleccionado = nombresCompletos.get(position);

            Intent intent = new Intent(ListaContactosActivity.this, Chats.class);
            intent.putExtra("nombre", nombreSeleccionado);
            intent.putExtra("a", "");      // Puedes pasar algún ID o valor adicional si lo necesitas
            intent.putExtra("from", "lista");  // Etiqueta para saber de dónde vino
            startActivity(intent);
        });
    }
}