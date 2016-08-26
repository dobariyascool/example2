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
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GcmListenerService;

public class GCMPushReceiverService extends GcmListenerService {

    String message,tickerText,contentTitle,subtitle;
    Bitmap largeBitmap = null;

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        message = data.getString("message");
        tickerText = data.getString("tickerText");
        contentTitle = data.getString("contentTitle");
        subtitle = data.getString("subtitle");
        String largeIconUrl = data.getString("largeIcon"); // the way you obtain this may differ

        try {
            largeBitmap = Glide
                    .with(this)
                    .load(largeIconUrl)
                    .asBitmap()
                    .into(100, 100) // Width and height
                    .get();


        } catch (Exception ex){
            // image download from the url failed
        }
        //Displaying a notiffication with the message
        sendNotification();
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
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
    }

}
