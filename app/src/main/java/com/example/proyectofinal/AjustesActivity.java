package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class AjustesActivity extends AppCompatActivity {

    private Switch switchNotificaciones, switchTemaOscuro, switchRecordatorios;
    private Spinner spinnerIdioma;
    private Button btnGuardarAjustes, btnCerrarSesion, btnChatsConUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Inicializar elementos
        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        switchTemaOscuro = findViewById(R.id.switchTemaOscuro);
        switchRecordatorios = findViewById(R.id.switchRecordatorios);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnChatsConUsuarios = findViewById(R.id.btnChatUsuarios);

        // Cargar idioma guardado
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        String idiomaGuardado = prefs.getString("Idioma", "es");

        // Opciones de idioma
        List<String> idiomas = Arrays.asList("Español", "English");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);

        // Seleccionar idioma actual
        spinnerIdioma.setSelection(idiomaGuardado.equals("es") ? 0 : 1);

        // Cambiar idioma al seleccionar
        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idiomaNuevo = position == 0 ? "es" : "en";
                if (!idiomaNuevo.equals(idiomaGuardado)) {
                    guardarIdioma(idiomaNuevo);
                    aplicarIdioma(idiomaNuevo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Aplicar idioma actual
        aplicarIdioma(idiomaGuardado);

        // Cargar switches
        cargarAjustes();

        // Botón guardar
        btnGuardarAjustes.setOnClickListener(v -> {
            boolean notificacionesActivadas = switchNotificaciones.isChecked();
            boolean temaOscuroActivo = switchTemaOscuro.isChecked();
            boolean recordatorioAutomatico = switchRecordatorios.isChecked();

            guardarAjustes(notificacionesActivadas, temaOscuroActivo, recordatorioAutomatico);

            if (recordatorioAutomatico) {
                programarRecordatorio();
            } else {
                cancelarRecordatorio();
            }

            String idioma = getSharedPreferences("AjustesPrefs", MODE_PRIVATE).getString("Idioma", "es");
            String mensaje = idioma.equals("es")
                    ? "Ajustes guardados:\n" +
                    "Notificaciones: " + (notificacionesActivadas ? "Activadas" : "Desactivadas") + "\n" +
                    "Tema Oscuro: " + (temaOscuroActivo ? "Activo" : "Inactivo") + "\n" +
                    "Recordatorio Automático: " + (recordatorioAutomatico ? "Activado" : "Desactivado")
                    : "Settings saved:\n" +
                    "Notifications: " + (notificacionesActivadas ? "Enabled" : "Disabled") + "\n" +
                    "Dark Mode: " + (temaOscuroActivo ? "On" : "Off") + "\n" +
                    "Auto Reminder: " + (recordatorioAutomatico ? "Enabled" : "Disabled");

            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });

        // Botón cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences prefsSesion = getSharedPreferences("sesion", MODE_PRIVATE);
            SharedPreferences.Editor editorSesion = prefsSesion.edit();
            editorSesion.clear();
            editorSesion.apply();

            Intent intent = new Intent(AjustesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Botón abrir chat
        btnChatsConUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(AjustesActivity.this, ChatUsuariosActivity.class);
            startActivity(intent);
        });
    }

    private void guardarAjustes(boolean notificaciones, boolean temaOscuro, boolean recordatorios) {
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Notificaciones", notificaciones);
        editor.putBoolean("TemaOscuro", temaOscuro);
        editor.putBoolean("Recordatorios", recordatorios);
        editor.apply();
    }

    private void cargarAjustes() {
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        switchNotificaciones.setChecked(prefs.getBoolean("Notificaciones", false));
        switchTemaOscuro.setChecked(prefs.getBoolean("TemaOscuro", false));
        switchRecordatorios.setChecked(prefs.getBoolean("Recordatorios", false));
    }

    @SuppressLint("ScheduleExactAlarm")
    private void programarRecordatorio() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        long tiempoAlarma = System.currentTimeMillis() + 60000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, tiempoAlarma, pendingIntent);

        Toast.makeText(this, "Recordatorio programado en 1 minuto", Toast.LENGTH_SHORT).show();
    }

    private void cancelarRecordatorio() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Recordatorio cancelado", Toast.LENGTH_SHORT).show();
    }

    private void guardarIdioma(String idioma) {
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Idioma", idioma);
        editor.apply();
    }

    private void aplicarIdioma(String idioma) {
        if (idioma.equals("es")) {
            setTitle("Ajustes de la Aplicación");
            switchNotificaciones.setText("Activar Notificaciones");
            switchTemaOscuro.setText("Tema Oscuro");
            switchRecordatorios.setText("Recordatorio Automático");
            btnGuardarAjustes.setText("Guardar Ajustes");
            btnCerrarSesion.setText("Cerrar Sesión");
            btnChatsConUsuarios.setText("Chat con Usuarios");
        } else {
            setTitle("Application Settings");
            switchNotificaciones.setText("Enable Notifications");
            switchTemaOscuro.setText("Dark Mode");
            switchRecordatorios.setText("Automatic Reminder");
            btnGuardarAjustes.setText("Save Settings");
            btnCerrarSesion.setText("Log Out");
            btnChatsConUsuarios.setText("Chat with Users");
        }
    }
}