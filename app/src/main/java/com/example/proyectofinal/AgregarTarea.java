package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AgregarTarea extends AppCompatActivity {

    EditText edtTitulo, edtDescripcion, edtFecha;
    Spinner spinnerGrupo;
    CheckBox chkRealizada;
    Button btnGuardar, btnCancelar;

    DBTareas dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        edtFecha = findViewById(R.id.edtFecha);
        chkRealizada = findViewById(R.id.chkRealizada);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBTareas(this);

        // Llenar el Spinner con los grupos de la base de datos
        cargarGruposEnSpinner();

        // Configurar selector de fecha
        edtFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AgregarTarea.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String fechaSeleccionada = selectedYear + "-"
                                + String.format("%02d", (selectedMonth + 1)) + "-"
                                + String.format("%02d", selectedDay);
                        edtFecha.setText(fechaSeleccionada);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnGuardar.setOnClickListener(v -> guardarTarea());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarGruposEnSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM " + DBTareas.TABLA_GRUPO, null);

        ArrayList<String> listaGrupos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                listaGrupos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaGrupos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
    }

    private void guardarTarea() {
        String titulo = edtTitulo.getText().toString();
        String descripcion = edtDescripcion.getText().toString();
        String fecha = edtFecha.getText().toString();
        boolean realizada = chkRealizada.isChecked();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerGrupo.getSelectedItem() == null) {
            Toast.makeText(this, "Debes seleccionar un grupo", Toast.LENGTH_SHORT).show();
            return;
        }

        String grupo = spinnerGrupo.getSelectedItem().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insertar la tarea
        ContentValues valores = new ContentValues();
        valores.put(DBTareas.COLUMNA_TITULO, titulo);
        valores.put(DBTareas.COLUMNA_DESCRIPCION, descripcion);
        valores.put(DBTareas.COLUMNA_GRUPO, grupo);
        valores.put(DBTareas.COLUMNA_FECHA_LIMITE, fecha);
        valores.put(DBTareas.COLUMNA_REALIZADA, realizada ? 1 : 0);

        long id = db.insert(DBTareas.TABLA_TAREAS, null, valores);

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
