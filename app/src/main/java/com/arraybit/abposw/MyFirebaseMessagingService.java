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
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String message, tickerText, contentTitle, subtitle;
    String title = "LikEat", notificationImage = "";
    int notificaiontype, offeMasterId, ItemMasterId, type, Id;
    Bitmap largeBitmap = null;

    public static Bitmap getBitmap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Toast.makeText(this, " " + remoteMessage.getData(), Toast.LENGTH_SHORT).show();
        try {
            if (remoteMessage.getNotification() != null) {
                message = remoteMessage.getNotification().getBody();
                title = remoteMessage.getNotification().getTitle();
            }

            if (remoteMessage.getData().size() > 0) {
                if (remoteMessage.getData().containsKey("type")) {
                    type = Integer.parseInt(remoteMessage.getData().get("type"));
                }
                if (remoteMessage.getData().containsKey("id")) {
                    Id = Integer.parseInt(remoteMessage.getData().get("id"));
                }
                if (remoteMessage.getData().containsKey("notificationImage")) {
                    notificationImage = remoteMessage.getData().get("notificationImage");
//                    if (notificationImage != null && !notificationImage.equals("")) {
//                        URL url = new URL(notificationImage);
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setDoInput(true);
//                        connection.connect();
//                        InputStream inputStream = connection.getInputStream();
//                        largeBitmap = BitmapFactory.decodeStream(inputStream);// Convert to bitmap
////                        largeBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    }
                }
//                largeBitmap = Glide
//                        .with(this)
//                        .load(notificationImage)
//                        .asBitmap()
//                        .into(100, 100) // Width and height
//                        .get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendNotification();
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        try {
            JSONObject json = new JSONObject(messageBody);
            message = json.getString("message");
            tickerText = json.getString("tickerText");
            contentTitle = json.getString("contentTitle");
            notificaiontype = json.getInt("notificationType");
            if (notificaiontype == 0) {
                offeMasterId = json.getInt("Id");
            } else if (notificaiontype == 1) {
                ItemMasterId = json.getInt("Id");
            }
            String largeIconUrl = json.getString("largeIcon"); // the way you obtain this may differ

            largeBitmap = Glide
                    .with(this)
                    .load(largeIconUrl)
                    .asBitmap()
                    .into(100, 100) // Width and height
                    .get();

            Intent intent;
            int requestCode = 0;
            if (notificaiontype == 2) {
                intent = new Intent(this, OfferDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("OfferMasterId", offeMasterId);
            } else if (notificaiontype == 1) {
                intent = new Intent(this, DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isBannerClick", true);
                intent.putExtra("ItemMasterId", ItemMasterId);
            } else {
                intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("isStart", true);
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
                    .setVibrate(new long[]{100, 250, 100, 250})
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification() {
        try {
            Intent intent;
            if (type == Globals.BannerType.Offer.getValue() && Id > 0) {
                intent = new Intent(this, OfferDetailActivity.class);
                intent.putExtra("OfferMasterId", Id);
            } else if (type == Globals.BannerType.Item.getValue() && Id > 0) {
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra("ItemMasterId", Id);
                intent.putExtra("isBannerClick", true);
            } else if (type == Globals.BannerType.Item.getValue() && Id > 0) {
                intent = new Intent(this, ItemListActivity.class);
                intent.putExtra("CateoryMasterId", Id);
                intent.putExtra("isBannerClick", true);
            } else {
                intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("isStart", true);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Bitmap bitmap = getBitmap(notificationImage);
            Toast.makeText(this, " bitmap :" +bitmap, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder notificationBuilder;
            if (bitmap != null) {
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_logo_likeat)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setVibrate(new long[]{100, 400, 100})
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_likeat))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);
            } else {
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_logo_likeat)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setVibrate(new long[]{100, 400, 100})
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_likeat))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);
            }
//            if (largeBitmap != null) {
//                Toast.makeText(this, "large image", Toast.LENGTH_SHORT).show();
//            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeBitmap));
//            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
//            }
//            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}