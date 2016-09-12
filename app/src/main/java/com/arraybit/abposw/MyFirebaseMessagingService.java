package com.arraybit.abposw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String message, tickerText, contentTitle, subtitle;
    int notificaiontype, offeMasterId, ItemMasterId;
    Bitmap largeBitmap = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        try {
            JSONObject json = new JSONObject(messageBody);
            message = json.getString("message");
            tickerText = json.getString("tickerText");
            contentTitle = json.getString("contentTitle");
            subtitle = json.getString("subtitle");
            notificaiontype = json.getInt("notificationType");
            if (notificaiontype == 0) {
                offeMasterId = json.getInt("itemOfferId");
            } else if (notificaiontype == 1) {
                ItemMasterId = json.getInt("itemOfferId");
            }
            String largeIconUrl = json.getString("largeIcon"); // the way you obtain this may differ

//            try {
                largeBitmap = Glide
                        .with(this)
                        .load(largeIconUrl)
                        .asBitmap()
                        .into(100, 100) // Width and height
                        .get();

//
//            } catch (Exception ex) {
//                // image download from the url failed
//            }


            Intent intent;
            int requestCode = 0;
            if(notificaiontype== 0)
            {
                intent = new Intent(this, OfferDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("OfferMasterId", offeMasterId);
            }
            else if(notificaiontype ==1)
            {
                intent = new Intent(this, DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isBannerClick", true);
                intent.putExtra("ItemMasterId", ItemMasterId);
            }
            else {
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Bitmap aBigBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle(contentTitle)
                    .setContentText(message)
                    .setTicker(tickerText)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_likeat))
                    .setVibrate(new long[]{100, 250, 100, 250, 100, 250})
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//
//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("Firebase Push Notification")
//                    .setContentText(messageBody)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}