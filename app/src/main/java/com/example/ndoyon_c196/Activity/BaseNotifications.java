package com.example.ndoyon_c196.Activity;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class BaseNotifications extends Application {
    public static final String COURSE_NOTIFICATION_CHANNEL = "COURSE_NOTIFICATION_CHANNEL";
    public static final String ASSESSMENT_NOTIFICATION_CHANNEL = "ASSESSMENT_NOTIFICATION_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        NotificationChannel course_notification_channel = new NotificationChannel(
                COURSE_NOTIFICATION_CHANNEL,
                "Course Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        course_notification_channel.setDescription("Reminder notifications for courses.");

        NotificationChannel assessment_notification_channel = new NotificationChannel(
                ASSESSMENT_NOTIFICATION_CHANNEL,
                "Assessment Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        assessment_notification_channel.setDescription("Reminder notifications for assessments");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(course_notification_channel);
        manager.createNotificationChannel(assessment_notification_channel);
    }
}