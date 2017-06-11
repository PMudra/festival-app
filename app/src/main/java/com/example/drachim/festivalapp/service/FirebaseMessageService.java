package com.example.drachim.festivalapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TEST", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TEST", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TEST", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // message will contain the Push Message
        String message = remoteMessage.getData().get("message");

        // festivalId will contain the id of the festival
        String festivalId = remoteMessage.getData().get("festivalId");

        sendNotification(message, festivalId);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, String festivalId) {

        Intent intent = new Intent(this, FestivalActivity.class);
        // TODO: Die Flags nochmal evaluieren bezügl. Backstack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(FestivalActivity.EXTRA_FESTIVAL_ID, Integer.parseInt(festivalId));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }
}
