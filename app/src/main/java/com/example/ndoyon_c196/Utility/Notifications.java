package com.example.ndoyon_c196.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.core.app.NotificationCompat;

import com.example.ndoyon_c196.R;

public class Notifications extends ContextWrapper {
    public static final String NOTIFICATION_CHANNEL = "ccome.example.ndoyon_c196.BaseApplication.NOTIFICATION_CHANNEL";
    public static final String NOTIFICATION_CHANNEL_NAME = "Course Reminder Notifications";

    private NotificationManager mManager;

    public Notifications(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {
        NotificationChannel course_notification_channel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        course_notification_channel.enableVibration(true);
        course_notification_channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(course_notification_channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }
}
