package com.example.gardenmanagmentapp.repository;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class FirebaseMessagingServiceRepository extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {

        // Check if message contains a data payload
        if (remoteMessage.getData().size() > 0) {
            String topic = remoteMessage.getData().get("topic");

            // Create notification
            String channelID = null;
            String messageTitle = null;
            final int NOTIFICATION_ID = 1;

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 26) {

                if(topic.equals("Chat")) {

                    Intent chat_message_intent = new Intent("message_received");
                    chat_message_intent.putExtra("message", remoteMessage.getData().get("message"));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(chat_message_intent);

                    channelID = "chat_message_notification";
                    messageTitle = "Chat message notification";
                    NotificationChannel chatChannel = new NotificationChannel("1", channelID, NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(chatChannel);
                }

                else if(topic.equals("Notifications")) {
                    Intent chat_message_intent = new Intent("notification_received");
                    chat_message_intent.putExtra("message", remoteMessage.getData().get("message"));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(chat_message_intent);

                    channelID = "chat_message_notification";
                    messageTitle = "Notification message";
                    NotificationChannel chatChannel = new NotificationChannel("1", channelID, NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(chatChannel);

                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
            builder.setContentTitle(messageTitle)
                    .setContentText(remoteMessage.getData().get("message"))
                    .setSmallIcon(android.R.drawable.star_on)
                    .setChannelId("1");
            manager.notify(NOTIFICATION_ID, builder.build());
        }

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {

        }

    }
}
