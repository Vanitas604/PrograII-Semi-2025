package com.example.proyectofinal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public final class NotificationUtils {

    private static final String CHANNEL_ID   = "taskplanner_channel";
    private static final String CHANNEL_NAME = "Recordatorios Task Planner";
    private static final int    NOTIF_ID     = 1001;

    /** Crea (o actualiza) el canal con sonido por defecto. */
    public static void createChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm.getNotificationChannel(CHANNEL_ID) != null) return;

            Uri sonido = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("Notificaciones de tareas y recordatorios");
            ch.enableLights(true);
            ch.enableVibration(true);
            ch.setSound(sonido, aa);
            nm.createNotificationChannel(ch);
        }
    }

    /** Muestra una notificación sencilla con sonido respetando la preferencia del usuario. */
    public static void show(Context ctx, String titulo, String cuerpo) {

        // 1. Verificar preferencia
        boolean habilitadas = ctx.getSharedPreferences("AjustesPrefs",
                Context.MODE_PRIVATE).getBoolean("Notificaciones", false);
        if (!habilitadas) return;

        // 2. Crear canal si es necesario
        createChannel(ctx);

        // 3. Intent que abre la app al tocar la notificación (opcional)
        Intent launchIntent = ctx.getPackageManager()
                .getLaunchIntentForPackage(ctx.getPackageName());
        PendingIntent pi = PendingIntent.getActivity(
                ctx, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT
                        | (Build.VERSION.SDK_INT >= 31 ? PendingIntent.FLAG_MUTABLE : 0));

        // 4. Construir notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif)          // Asegúrate de tener este icono
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE);

        NotificationManagerCompat.from(ctx).notify(NOTIF_ID, builder.build());
    }
}

