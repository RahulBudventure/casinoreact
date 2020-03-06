package com.budventure.newskedit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.budventure.newskedit.AccessbilityServices.MyAccessibilityService;
import com.budventure.newskedit.JBServices.OpenPlayStoretJobService;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnEnable;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    MyReceiver myReceiver;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiver=new MyReceiver();

       // timePicker = (TimePicker) findViewById(R.id.timePicker);

        btnEnable=(AppCompatButton)findViewById(R.id.btnenable);
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 1);
                if (!isAccessibilityOn (MainActivity.this, MyAccessibilityService.class)) {
                    Intent intent = new Intent (Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity (intent);
                }else {
                    PackageManager pm = getPackageManager();

                    try
                    {
                        // Raise exception if whatsapp doesn't exist
                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                        Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        waIntent.setPackage("com.whatsapp");
                        waIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
                        startActivity(waIntent);
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        Toast.makeText(MainActivity.this, "Please install whatsapp app", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

               /* Calendar calendar = Calendar.getInstance();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }

                setAlarm(calendar.getTimeInMillis());*/


            }
        });

       // this.registerReceiver(myReceiver,new IntentFilter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Intent scheduledService = new Intent(context, OpenPlayStoretJobService.class);
           /// getApplicationContext().startService(scheduledService);
            Intent scheduledService = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.example.android"));
            context.startActivity(scheduledService);


        }
    }


    private void setAlarm(long time) {
        //getting the alarm manager
        startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 1);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyReceiver.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==1){

                Intent scheduledService = new Intent(Intent.ACTION_VIEW);
                scheduledService.setData(Uri.parse("market://details?id=com.example.android"));
                startActivity(scheduledService);
            }
        }
    }

    private boolean isAccessibilityOn (Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + clazz.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

