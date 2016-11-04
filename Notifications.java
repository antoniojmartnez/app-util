package com.ibs.tecnicos.seyte.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class Notifications {
    private App app;
    private NotificationManager manager;

    int largeIcon;
    int smallIcon;

    public Notifications (int largeIcon, int smallIcon, App app) {
        this.app = app;

        this.manager = (NotificationManager) this.app.getSystemService(Context.NOTIFICATION_SERVICE);
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
    }

    /**
     * Crea una nueva notificación en el centro de notificaciones
     *
     * @param title Título de la notificación
     * @param content Contenido de la notificación
     * @param intent Intent que se lanzará al pulsar la notificación
     */
    public void createNotification (String title, String content, Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(this.app.getContext(), 0, intent, 0);

        Bitmap icon = BitmapFactory.decodeResource(this.app.getContext().getResources(), this.largeIcon);
        int width = (int) this.app.getContext().getResources().getDimension(android.R.dimen.notification_large_icon_width) / 2;
        int height = (int) this.app.getContext().getResources().getDimension(android.R.dimen.notification_large_icon_height) / 2;
        icon = Bitmap.createScaledBitmap(icon, width, height, false);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.app.getContext())
                        .setLargeIcon(icon)
                        .setSmallIcon(this.smallIcon)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(content))
                        .setContentText(content);

        mBuilder.setContentIntent(contentIntent);
        manager.notify(0, mBuilder.build());
    }

    /**
     * Borra todas las notificaciones de la aplicación que hay en el centro de notificaciones
     */
    public void cancelAll () {
        manager.cancelAll();
    }
}