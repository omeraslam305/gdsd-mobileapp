package com.example.omer.testapplication.classes.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.omer.testapplication.UserMessagesActivity;
import com.example.omer.testapplication.classes.Session;

import org.apache.http.NameValuePair;

import java.util.List;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.example.omer.testapplication.classes.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getAndroidChannelNotification(String title, String body,List<NameValuePair> params) {
        Intent intent = new Intent(this, UserMessagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String conversationID = params.get(0).getValue();
        String senderIdVal = params.get(1).getValue();
        String senderNameVal = params.get(2).getValue();
        String receiverIdVal = params.get(3).getValue();
        String receiverNameVal = params.get(4).getValue();

        int userId = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId;
        String name = "", senderId = "";
        try{
            if(userId ==  Integer.parseInt(senderIdVal)){
                name = receiverNameVal;
            } else
                name = senderNameVal;

            if(userId ==  Integer.parseInt(senderIdVal)){
                senderId = receiverIdVal;
            } else
                senderId = senderIdVal;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        intent.putExtra("ConId", conversationID);
        intent.putExtra("OtherPerson", name);
        intent.putExtra("OtherPersonId", senderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_ONE_SHOT);
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }
}