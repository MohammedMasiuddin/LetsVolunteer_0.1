package com.example.letsvolunteer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

public class notification_user extends AppCompatActivity {
    private static RemoteViews contentView;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static final int NotificationID = 1005;
    private static NotificationCompat.Builder mBuilder;

    @SuppressLint("RemoteViewLayout")
    private void RunNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        contentView = new RemoteViews(getPackageName(), R.layout.activity_notification_user);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        Intent switchIntent = new Intent(this,
                BackgroundService.switchButtonListener.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 1020, switchIntent, 0);
      //  contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
      //  mBuilder.setSmallIcon(R.mipmap.newicon);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notification = mBuilder.build();
        notificationManager.notify(NotificationID, notification);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_user);
    }
}