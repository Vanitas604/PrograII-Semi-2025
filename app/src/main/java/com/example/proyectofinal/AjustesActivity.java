package com.example.proyectofinal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {

    private Switch switchNotificaciones, switchTemaOscuro, switchRecordatorios, switchChats, switchIdioma;
    private Button btnGuardarAjustes;
    private Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Inicializando los componentes
        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        switchTemaOscuro = findViewById(R.id.switchTemaOscuro);
        switchRecordatorios = findViewById(R.id.switchRecordatorios);
        switchChats = findViewById(R.id.switchChats);
        switchIdioma = findViewById(R.id.switchIdioma);
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Cargar los valores guardados al iniciar la actividad
        cargarAjustes();

        // Botón guardar ajustes
        btnGuardarAjustes.setOnClickListener(v -> {
            boolean notificacionesActivadas = switchNotificaciones.isChecked();
            boolean temaOscuroActivo = switchTemaOscuro.isChecked();
            boolean recordatorioAutomatico = switchRecordatorios.isChecked();
            boolean chatsActivados = switchChats.isChecked();
            boolean idiomaEspanol = switchIdioma.isChecked();

            guardarAjustes(notificacionesActivadas, temaOscuroActivo, recordatorioAutomatico, chatsActivados, idiomaEspanol);

            if (recordatorioAutomatico) {
                programarRecordatorio();
            } else {
                cancelarRecordatorio();
            }

            String mensaje = "Ajustes guardados:\n" +
                    "Notificaciones: " + (notificacionesActivadas ? "Activadas" : "Desactivadas") + "\n" +
                    "Tema Oscuro: " + (temaOscuroActivo ? "Activo" : "Inactivo") + "\n" +
                    "Recordatorio Automático: " + (recordatorioAutomatico ? "Activado" : "Desactivado") + "\n" +
                    "Chats: " + (chatsActivados ? "Activados" : "Desactivados") + "\n" +
                    "Idioma: " + (idiomaEspanol ? "Español" : "Otro");

            Toast.makeText(AjustesActivity.this, mensaje, Toast.LENGTH_LONG).show();
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
    }

    private void guardarAjustes(boolean notificaciones, boolean temaOscuro, boolean recordatorios, boolean chats, boolean idioma) {
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Notificaciones", notificaciones);
        editor.putBoolean("TemaOscuro", temaOscuro);
        editor.putBoolean("Recordatorios", recordatorios);
        editor.putBoolean("Chats", chats);
        editor.putBoolean("Idioma", idioma);
        editor.apply();
    }

    private void cargarAjustes() {
        SharedPreferences prefs = getSharedPreferences("AjustesPrefs", MODE_PRIVATE);
        switchNotificaciones.setChecked(prefs.getBoolean("Notificaciones", false));
        switchTemaOscuro.setChecked(prefs.getBoolean("TemaOscuro", false));
        switchRecordatorios.setChecked(prefs.getBoolean("Recordatorios", false));
        switchChats.setChecked(prefs.getBoolean("Chats", false));
        switchIdioma.setChecked(prefs.getBoolean("Idioma", true)); // Español por defecto
    }

    private void programarRecordatorio() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long tiempoAlarma = System.currentTimeMillis() + 60000; // 1 minuto desde ahora para prueba

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, tiempoAlarma, pendingIntent);

        Toast.makeText(this, "Recordatorio programado en 1 minuto", Toast.LENGTH_SHORT).show();
    }

    private void cancelarRecordatorio() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Recordatorio cancelado", Toast.LENGTH_SHORT).show();
    }
}