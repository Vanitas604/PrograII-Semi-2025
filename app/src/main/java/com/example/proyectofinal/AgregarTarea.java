package com.example.proyectofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarTarea extends AppCompatActivity {

    EditText edtTitulo, edtDescripcion, edtGrupo, edtFecha;
    CheckBox chkRealizada;
    Button btnGuardar, btnCancelar;

    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtGrupo = findViewById(R.id.edtGrupo);
        edtFecha = findViewById(R.id.edtFecha);
        chkRealizada = findViewById(R.id.chkRealizada);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBTareas(this);

        btnGuardar.setOnClickListener(v -> guardarTarea());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarTarea() {
        String titulo = edtTitulo.getText().toString();
        String descripcion = edtDescripcion.getText().toString();
        String grupo = edtGrupo.getText().toString();
        String fecha = edtFecha.getText().toString();
        boolean realizada = chkRealizada.isChecked();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase DBTareas = dbHelper.getWritableDatabase();

        // Verificar si el grupo ya existe
        Cursor cursor = DBTareas.rawQuery("SELECT * FROM " + DBTareas.TABLA_GRUPO + " WHERE nombre = ?", new String[]{grupo});
        if (!cursor.moveToFirst()) {
            // Grupo no existe, lo insertamos
            ContentValues valoresGrupo = new ContentValues();
            valoresGrupo.put("nombre", grupo);
            DBTareas.insert(DBTareas.TABLA_GRUPO, null, valoresGrupo);
        }
        cursor.close();

        // Insertar la tarea
        ContentValues valores = new ContentValues();
        valores.put(DBTareas.COLUMNA_TITULO, titulo);
        valores.put(DBTareas.COLUMNA_DESCRIPCION, descripcion);
        valores.put(DBTareas.COLUMNA_GRUPO, grupo);
        valores.put(DBTareas.COLUMNA_FECHA_LIMITE, fecha);
        valores.put(DBTareas.COLUMNA_REALIZADA, realizada ? 1 : 0);

        long id = DBTareas.insert(DBTareas.TABLA_TAREAS, null, valores);

        if (id > 0) {
            Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(this, "Modificar seleccionado", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.mnxEliminar) {
            Toast.makeText(this, "Eliminar seleccionado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



