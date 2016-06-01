package com.example.rahat_pdm.policeapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        String wholeMessage = data.getString("message");
        String location, clue, phoneno;


        if (wholeMessage.contains("-"))
        {
            String fullMessage[] = wholeMessage.split("-", 3);
            location = fullMessage[0];
            clue = fullMessage[1];
            phoneno = fullMessage[2];

        }
        else
        {
            location = wholeMessage;
            clue = "no clue available for crime";
            phoneno = "";

        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "location: " + location);
        Log.d(TAG, "clue: " + clue);
        Log.d(TAG, "phoneno: " + phoneno);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(location,phoneno, clue);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String location, String phoneno, String clue)
    {

        String fullMessage[] = location.split(",", 2);
        String lat = fullMessage[0];
        String lng = fullMessage[1];


        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.putExtra("lat",lat);
        resultIntent.putExtra("lng", lng);

        PendingIntent resultPendingIntent =

                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  *//*Request code*//*, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("A victim found" + " " + clue)
                .setContentText(phoneno)
                        //.setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

