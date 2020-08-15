package com.dpoint.dpointsuser.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.dpoint.dpointsuser.R;
import com.dpoint.dpointsuser.preferences.UserPreferences;
import com.dpoints.view.module.dashboard.Dashboard;
import com.dpoint.dpointsuser.view.module.splash.OnBoardingActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    Bitmap bitmap;
    String clickAction;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            getImage(remoteMessage);
            // clickAction = remoteMessage.getNotification().getClickAction();
        }
    }
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap);
        }
        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            sendNotification(null);
        }
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private void getImage(final RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
      //  Log.e(TAG, "title :- " + data.get("title"));
        Config.title = data.get("title");
       // Log.e(TAG, "urlToImage :- " + data.get("image"));
        Config.urlToImage = data.get("image");
        Config.content = data.get("body");
        //Create thread to fetch image from notification
        if (remoteMessage.getData() != null) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                   if(!Config.urlToImage.trim().equals("")){
                       Picasso.get()
                               .load(Config.urlToImage).placeholder(R.drawable.bg_dash)
                               .into(target);
                   }else{
                       sendNotification(null);
                   }

                }
            });
        }
    }
    private void sendNotification(Bitmap bitmap) {
         Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Intent intent = new Intent(getApplicationContext(), HomeActivity.class)
        Intent intent=null;
        if(UserPreferences.Companion.getInstance().isLoggedIn(this)){
             intent = new Intent(this, Dashboard.class);
             intent.putExtra("NOTIFICATION","YES");
        }else{
            intent = new Intent(this, OnBoardingActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("News Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.dpoint_logo)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0L});
        if(bitmap!=null){
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.bigPicture(bitmap);
            notificationBuilder.setStyle(style);
        }
        notificationBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
