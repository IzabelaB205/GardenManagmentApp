package com.example.gardenmanagmentapp.database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class FirebaseMessagingServiceRepository extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {

        // Check if message contains a data payload
        if(remoteMessage.getData().size() > 0) {
            Intent intent = new Intent("message_received");
            intent.putExtra("message", remoteMessage.getData().get("message"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Create notification
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);

            if(Build.VERSION.SDK_INT >= 26) {

                NotificationChannel channel = new NotificationChannel("1", "chat_message_notification", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId("1");
            }

            builder.setContentTitle("New chat message").setContentText(remoteMessage.getData().get("message")).setSmallIcon(android.R.drawable.star_on);
            manager.notify(1, builder.build());
        }

        // Check if message contains a notification payload
        if(remoteMessage.getNotification() != null) {

        }

    }
}
