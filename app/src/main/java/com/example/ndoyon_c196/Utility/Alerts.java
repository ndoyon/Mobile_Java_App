package com.example.ndoyon_c196.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Alerts extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationTitle = intent.getStringExtra("NOTIFICATION_TITLE");
        String notificationMessage = intent.getStringExtra("NOTIFICATION_MESSAGE");

        Notifications notificationHelper = new Notifications(context);
        NotificationCompat.Builder nb = notificationHelper.getNotification(notificationTitle, notificationMessage);
        notificationHelper.getManager().notify(createID(), nb.build());
    }
    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

}
