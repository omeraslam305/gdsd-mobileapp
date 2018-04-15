package com.example.omer.testapplication.classes.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.classes.Session;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.omer.testapplication.MainActivity;
import com.example.omer.testapplication.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationUtils mNotificationUtils;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

        mNotificationUtils = new NotificationUtils(this);

        Boolean userLoggedIn = Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
        if(userLoggedIn){
            int userId = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId;
            if(userId == Integer.parseInt(remoteMessage.getData().get("ReceiverId"))){
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ConId", remoteMessage.getData().get("ConId")));
                nameValuePairs.add(new BasicNameValuePair("SenderId", remoteMessage.getData().get("SenderId")));
                nameValuePairs.add(new BasicNameValuePair("SenderName", remoteMessage.getData().get("SenderName")));
                nameValuePairs.add(new BasicNameValuePair("ReceiverId", remoteMessage.getData().get("ReceiverId")));
                nameValuePairs.add(new BasicNameValuePair("ReceiverName", remoteMessage.getData().get("ReceiverName")));

                Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),nameValuePairs);
                mNotificationUtils.getManager().notify(101, nb.build());
            }

        }


    }
}