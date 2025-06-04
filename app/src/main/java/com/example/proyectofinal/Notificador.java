package com.example.proyectofinal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notificador {

    private static final String CHANNEL_ID = "canal_tareas";

    public static void crearCanalNotificacion(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal Tareas";
            String description = "Canal para notificaciones de tareas";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void mostrarNotificacion(Context context, String titulo, String mensaje, String grupo) {
        crearCanalNotificacion(context);

        String mensajeCompleto = mensaje;
        if (grupo != null && !grupo.isEmpty()) {
            mensajeCompleto += " (Grupo: " + grupo + ")";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(mensajeCompleto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context)
                .notify((int) System.currentTimeMillis(), builder.build());
    }

    public static void mostrarNotificacion(Context context, String titulo, String mensaje) {
        mostrarNotificacion(context, titulo, mensaje, null);
    }
}
