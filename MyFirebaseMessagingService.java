package com.oozeetech.bizdesk.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.oozeetech.bizdesk.R;
import com.oozeetech.bizdesk.models.notification.NotificationData;
import com.oozeetech.bizdesk.ui.drawer.DrawerActivity;
import com.oozeetech.bizdesk.utils.Preferences;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Preferences pref;
    Map<String, String> data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        pref = new Preferences(getApplicationContext());
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        data = remoteMessage.getData();
        NotificationData notificationData = new NotificationData();
        notificationData.setType(data.get("Type"));
        notificationData.setBizMasterID(data.get("BizMasterID"));
        notificationData.setUserID(data.get("UserID"));
        sendNotification(notification);

    }

    private void sendNotification(RemoteMessage.Notification notification) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent intent;
        intent = new Intent(this, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        intent.putExtra("body", data);

//        Bundle bundle = new Bundle();
//        bundle.putString("picture_url", data.getSize("picture_url"));
//        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle(notification.getTitle())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                        .setContentInfo("")
                        .setLargeIcon(icon)
                        .setColor(Color.TRANSPARENT)
                        .setContentIntent(pendingIntent)
                        .setContentText(notification.getBody())
                        .setLights(Color.TRANSPARENT, 1000, 300)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(notification.getBody());
        bigText.setBigContentTitle(notification.getTitle());
        bigText.setSummaryText("By : BizDesk");
        mBuilder.setStyle(bigText);

//        try {
//            String picture_url = data.getSize("picture_url");
//            if (picture_url != null && !"".equals(picture_url)) {
//                URL url = new URL(picture_url);
//                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());

    }

}
