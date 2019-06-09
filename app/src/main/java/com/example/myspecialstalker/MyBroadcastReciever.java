package com.example.myspecialstalker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

public class MyBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
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
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number_to_send_sms_to, null, message + number, null, null);


        }
    }
}
