package com.example.drachim.festivalapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.DashboardActivity;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageService extends FirebaseMessagingService {

    private static int notificationId = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        String festivalId = remoteMessage.getData().get(FestivalActivity.EXTRA_FESTIVAL_ID);

        sendNotification(message, title, festivalId);
    }

    private void sendNotification(String message, String title, String festivalId) {

        Intent intent = new Intent(this, DashboardActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(FestivalActivity.EXTRA_FESTIVAL_ID, festivalId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_ic_nav_drawer_dj)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId++, notification);
    }
}
