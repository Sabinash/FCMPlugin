package com.gae.scaffolder.plugin;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meritmerge.phonegap.R;

import org.json.JSONObject;
import static android.R.attr.data;
import static android.R.attr.key;

/**
 * Created by Felipe Echanique on 08/06/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMPlugin";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "==> MyFirebaseMessagingService onMessageReceived");



       // data=object.getString("order_id");
      //  JSONObject object = new JSONObject(params);
        //Log.e("JSON_OBJECT", object.toString());

		if( remoteMessage.getNotification() != null){
			Log.d(TAG, "\tNotification Title: " + remoteMessage.getNotification().getTitle());
			Log.d(TAG, "\tNotification Message: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "\tNotification: " + remoteMessage.getNotification().toString());
           // JSONObject object = new JSONObject(remoteMessage.getData());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("wasTapped", false);



        for (String key : remoteMessage.getData().keySet()) {
                Object value = remoteMessage.getData().get(key);
                Log.d(TAG, "\tKey: " + key + " Value: " + value);
				data.put(key, value);
        }
        data.put("body",remoteMessage.getNotification().getBody());

		Log.d(TAG, "\tNotification Data: " + data.toString());
      //  sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),NotificationID.getID());
        //FCMPlugin.sendPushPayload( data );
        //sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), data);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody, int notificationId, Intent myIntent) {
        Intent intent = new Intent(this, FCMPluginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		/*for (String key : data.keySet()) {
			intent.putExtra(key, data.get(key).toString());
		}*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);




        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.icon))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);



        notificationBuilder.setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }


    @Override
    public void handleIntent(Intent intent){

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("wasTapped", false);

        Intent notifyIntent = new Intent(this, FCMPluginActivity.class);

       // notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if (intent.getExtras() != null) {
            Log.d(TAG, "==> USER TAPPED NOTFICATION");
          //  data.put("wasTapped", true);
            for (String key : intent.getExtras().keySet()) {
                String value = intent.getExtras().getString(key);
                Log.d(TAG, "\tKey: " + key + " Value: " + value);
                data.put(key, value);
                notifyIntent.putExtra(key, value);
            }
            notifyIntent.putExtra("body",data.get("body").toString());
           /* PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);*/

            sendNotification(data.get("title").toString(),data.get("body").toString(),NotificationID.getID(),notifyIntent);

          SqliteController mSqliteController = new SqliteController(getApplicationContext());
          Date currentDate = new Date();
          String dateToStr = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(currentDate);
            Notifications mNotification = new Notifications();
            mNotification.setMessageId(data.get("messageId").toString());
            mNotification.setTitle(data.get("title").toString());
            mNotification.setMessage(data.get("body").toString());
            mNotification.setMessageDate(dateToStr);
            mSqliteController.insertNotification(mNotification);

          FCMPlugin.sendPushPayload( data );
        }
    }
    public static class NotificationID {
        private static final  AtomicInteger c = new AtomicInteger(0);
        public  static int getID() {
            return c.incrementAndGet();
        }
    }
}
