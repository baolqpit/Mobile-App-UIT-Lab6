package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class hw1 extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    public void processReceive(Context context, Intent intent){
        Toast.makeText(context, getString(R.string.you_have_a_new_message), Toast.LENGTH_SHORT).show();
        TextView tvContent = findViewById(R.id.textView);
        final String SMS_EXTRA = "pdus";
        Bundle bundle = intent.getExtras();
        Object[] messages = (Object[]) bundle.get(SMS_EXTRA);
        String sms = "";
        SmsMessage smsMessage;
        for (int i = 0; i < messages.length; i++){
            if (Build.VERSION.SDK_INT >= 23){
                smsMessage = SmsMessage.createFromPdu((byte[]) messages[i], "");
            } else {
                smsMessage = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            String msgBody = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();
            sms += address + ":\n" + msgBody + "\n";
        }
        tvContent.setText(sms);
    }

    private void initBroadcastReceiver(){
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processReceive(context, intent);
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw1);
        initBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver == null) initBroadcastReceiver();

        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}