package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textBattery, chargeText;
    private Button b;
    private int isPercentage;
    private PowerConnectionReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.button);
        textBattery =(TextView) findViewById(R.id.batteryText);
        chargeText = (TextView) findViewById(R.id.batteryText2);
    }
    public void check(View v){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        BatteryManager bm=(BatteryManager)getSystemService(BATTERY_SERVICE);
        isPercentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;

        if(isFull)
        {
            textBattery.setText("Battery is full!!!");
        }
        else {
            textBattery.setText("Device is " + isPercentage + "% charge");
        }

        // Are we charging / charged?
        int status2 = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if(usbCharge)
            chargeText.setText("USB charging");
        if(acCharge)
            chargeText.setText("AC charging");
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onStart() {
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}