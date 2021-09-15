package com.comma_store.shopping;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.Navigation;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.ui.Home.HomeFragmentDirections;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SharedPreferences preferences;
    int notificationId;
    HomeFragmentDirections.ActionHomeFragmentToGetItemsGraph action;

    @Override
    public void onNewToken(@NonNull String deviceToken) {
        Log.i("device_token",deviceToken);
//        preferences = getSharedPreferences(SharedPreferencesUtils.SharedPrefranceName, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SharedPreferencesUtils.DeviceToken,deviceToken);
//        editor.apply();
        SharedPreferencesUtils.getInstance(getApplicationContext()).setDeviceToken(deviceToken);
        //cache token for sending in login and register.
        //check if logged in call add device token endpoint(optional).
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification()!=null){
            Log.i("notificationType","notification");
        }
        Map<String,String> data = remoteMessage.getData();
        int notificationType = Integer.parseInt(data.get("type_n"));
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, MainActivity.class);
        intent.putExtra("id",Integer.parseInt(remoteMessage.getData().get("reference_id")));
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        switch (notificationType){
            case 0:
                intent.putExtra("navigation","promoCode");
                break;
            case 1:
                intent.putExtra("navigation","order");
                break;
            case 2:
                intent.putExtra("navigation","offers_sub");
                break;
        }
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        //FLAG_ONE_SHOT
        //createNotificationChannels();
        int langKey = SharedPreferencesUtils.getInstance(getApplicationContext()).getLangKey();
        if(langKey==0){
            showNotification(data.get("title_en"),data.get("body_en"),pendingIntent);
        }else {
            showNotification(data.get("title_ar"),data.get("body_ar"),pendingIntent);
        }

        //handle data messages cames from server
        Log.i("notificationType",""+notificationType);
    }


    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.ic_notification);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message,
                                 PendingIntent pendingIntent) {

        // Assign channel ID
        String channel_id = "notification_channel";
        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setGroup("store")
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        }
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_notification);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }
        notificationManager.notify(1, builder.build());
        notificationId++;
    }
}
