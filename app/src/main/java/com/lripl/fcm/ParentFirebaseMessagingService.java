package com.lripl.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.R;
import com.lripl.utils.Constants;

public class ParentFirebaseMessagingService extends FirebaseMessagingService {
   private static String TAG = ParentFirebaseMessagingService.class.getName();
    @Override
    public void onNewToken(String s) {
        Log.d(TAG+"-------------FCM_TOKEN", s);
        SharedPrefsHelper.getInstanse(this).put(Constants.PREF_KEYS.DEVICE_TOKEN.name(),s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "RemoteMessage data : " + remoteMessage.getData() + " data size ()  :  " + remoteMessage.getData().size() + " : " + remoteMessage.getData().get("type")+" : "+remoteMessage.getData().get("value")+" : "+remoteMessage.getData().get("msg") );
        sendNotification(remoteMessage.getData().get("msg"));
    }
    private void sendNotification(String messageBody) {
        final String CHANNEL_ID = "com.lripl.main.CHANNEL_ID";
        final String CHANNEL_NAME = "Lripl CHANNEL";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notifChannel.enableLights(true);
            notifChannel.enableVibration(true);
            notifChannel.setLightColor(Color.GRAY);
            notifChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notifChannel);
        }
        int requestCode = ("BabyMonitor" + System.currentTimeMillis()).hashCode();
        Intent intent = new Intent(this, ItemsTypeListActivity.class);;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setColor(getResources().getColor(R.color.transparent_new))
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        //notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        setNotificationColor(notificationBuilder);
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notificationBuilder.build());
        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        /**/
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    private void setNotificationColor(NotificationCompat.Builder notificationBuilder) {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        if (useWhiteIcon) {
            notificationBuilder.setColor(getResources().getColor(R.color.transparent_new));
        } else {
            notificationBuilder.setColor(getResources().getColor(R.color.white));
        }
    }
}
