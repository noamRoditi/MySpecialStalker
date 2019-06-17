package com.example.myspecialstalker;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;

public class MyBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            NotificationHandler.createChannel(context);
            SharedPreferences sharedPreferences = MainActivity.getSharedPreferences();
            String number_to_send_sms_to;
            String message;
            if(sharedPreferences.contains("number") || !sharedPreferences.contains("text")){
                return;
            }
            message = sharedPreferences.getString("message","");
            number_to_send_sms_to = sharedPreferences.getString("number","");
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).toString();
            Log.w("", "Number---->"+number);
            NotificationHandler.myCreateNotification("MySpecialStalker","sending message...",context);
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent piSent=PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
            PendingIntent piDelivered= PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
            smsManager.sendTextMessage(number_to_send_sms_to, null, message + number, piSent, piDelivered);
        }

    }

}
