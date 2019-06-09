package com.example.myspecialstalker;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private EditText editText_number;
    private EditText editText_message;
    private TextView editText_warning;
    private static SharedPreferences sharedPreferences;
    private MyBroadcastReciever myBroadcastReciever;

    String[] appPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        editText_number = (EditText)findViewById(R.id.editText_number);
        editText_message = (EditText)findViewById(R.id.editText_message);
        editText_warning = (TextView)findViewById(R.id.textView);
        myBroadcastReciever = new MyBroadcastReciever();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(myBroadcastReciever, filter);
        sharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        if(!arePermissionsEnabled()){
            requestMultiplePermissions();
        }
        addTextWatchers();
    }

    public boolean arePermissionsEnabled(){
        for(String permission : appPermissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
    private void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : appPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    if(shouldShowRequestPermissionRationale(permissions[i])){
                        AlertDialog dialog= new AlertDialog.Builder(this)
                                .setMessage("Please approve all permissions we really need them")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestMultiplePermissions();
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                    return;
                }
            }
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(sharedPreferences.contains("number")){
            editText_number.setText(sharedPreferences.getString("number",""));
        }
        if(sharedPreferences.contains("text")){
            editText_message.setText(sharedPreferences.getString("message",""));
        }
    }
    private void addTextWatchers(){
        editText_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((editText_message.getText().length() > 0)
                        &&(editText_number.getText().length() > 0)){
                    editText_warning.setText("App is ready to send SMS");
                }else{
                    editText_warning.setText("Please fill all the information");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String number = editText_message.getText().toString();
                editor.putString("message", number);
                editor.apply();
            }
        });
        editText_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((editText_message.getText().length() > 0)
                        &&(editText_number.getText().length() > 0)){
                    editText_warning.setText("App is ready to send SMS");
                }else{
                    editText_warning.setText("Please fill all the information");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String number = editText_number.getText().toString();
                editor.putString("number", number);
                editor.apply();
            }
        });
    }
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReciever);
    };
}
