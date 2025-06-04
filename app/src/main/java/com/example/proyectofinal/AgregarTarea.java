package com.example.proyectofinal;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class AgregarTarea extends AppCompatActivity {

    private EditText edtTitulo, edtDescripcion, edtFecha, edtFechaRecordatorio, edtHoraRecordatorio;
    private Spinner spinnerGrupo;
    private CheckBox chkRealizada, chkRepetirDiario;
    private Button btnGuardar, btnCancelar;
    private DBTareas dbTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        dbTareas = new DBTareas(this);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtFecha = findViewById(R.id.edtFecha);
        edtFechaRecordatorio = findViewById(R.id.edtFechaRecordatorio);
        edtHoraRecordatorio = findViewById(R.id.edtHoraRecordatorio);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        chkRealizada = findViewById(R.id.chkRealizada);
        chkRepetirDiario = findViewById(R.id.chkRepetirDiario);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        edtFecha.setOnClickListener(v -> mostrarDatePicker(edtFecha));
        edtFechaRecordatorio.setOnClickListener(v -> mostrarDatePicker(edtFechaRecordatorio));
        edtHoraRecordatorio.setOnClickListener(v -> mostrarTimePicker(edtHoraRecordatorio));

        btnGuardar.setOnClickListener(v -> guardarTarea());
        btnCancelar.setOnClickListener(v -> finish());

        cargarGruposEnSpinner();
    }

    private void mostrarDatePicker(EditText campo) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> campo.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void mostrarTimePicker(EditText campo) {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> campo.setText(String.format("%02d:%02d", hourOfDay, minute)),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void cargarGruposEnSpinner() {
        List<String> grupos = new ArrayList<>();
        SQLiteDatabase db = dbTareas.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM grupo", null);

        if (cursor.moveToFirst()) {
            do {
                grupos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if (grupos.isEmpty()) {
            grupos.add("Sin grupos disponibles");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
    }

    private void guardarTarea() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();
        String grupo = spinnerGrupo.getSelectedItem() != null ? spinnerGrupo.getSelectedItem().toString() : "";
        String fechaLimite = edtFecha.getText().toString().trim();
        String fechaRecordatorio = edtFechaRecordatorio.getText().toString().trim();
        String horaRecordatorio = edtHoraRecordatorio.getText().toString().trim();
        boolean realizada = chkRealizada.isChecked();
        boolean repetirDiariamente = chkRepetirDiario.isChecked();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (grupo.equals("Sin grupos disponibles")) {
            Toast.makeText(this, "Primero debes crear un grupo", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbTareas.getWritableDatabase();
        db.execSQL("INSERT INTO tareas (titulo, descripcion, grupo, fecha_limite, realizada, hora_recordatorio, repetir_diariamente) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{titulo, descripcion, grupo, fechaLimite, realizada ? 1 : 0, horaRecordatorio, repetirDiariamente ? 1 : 0});
        db.close();

        // Programar notificación
        programarNotificacion(this, fechaRecordatorio, horaRecordatorio);

        Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void programarNotificacion(Context context, String fecha, String hora) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date fechaHora = sdf.parse(fecha + " " + hora);

            if (fechaHora != null && fechaHora.after(new Date())) {
                Intent intent = new Intent(context, Notificador.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, fechaHora.getTime(), pendingIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
