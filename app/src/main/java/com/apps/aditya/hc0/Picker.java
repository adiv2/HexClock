package com.apps.aditya.hc0;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Picker extends AppCompatActivity
{
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
    }

    public void buzz2(View view)
    {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String ampm ="AM";
        Context context =getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        int s = Calendar.getInstance().get(Calendar.SECOND);
        int ringMin = timePicker.getMinute();
        int ringHour = timePicker.getHour();
        int displayHour = ringHour;
        int displayMin = ringMin;
        if(ringHour==0 && h!=0){ringHour=24;}
        if(ringHour<h){ringHour=ringHour+24;}
        if(ringHour==h && ringMin<m){ringHour=ringHour+24;}
        ringMin = (ringMin*60)-(m*60)-s;
        ringHour = (ringHour*60*60)-(h*60*60);
        ringMin = ringMin + ringHour;
        if(displayHour>11){ampm="PM";}
        if(displayHour==0){displayHour=12;}
        if(displayHour>12){displayHour=displayHour-12;}
        CharSequence text = "Alarm set for "+displayHour+" : "+displayMin+" "+ampm;
        editor.putInt("ringHour",displayHour);
        editor.putInt("ringMin",displayMin);
        editor.putString("ampm",ampm);
        editor.commit();
        Intent intent = new Intent(Picker.this, FullscreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent pendingIntent = PendingIntent.getActivity(Picker.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis()+ ringMin*1000), pendingIntent);

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent1 = new Intent(this,MainActivity.class);
        startActivity(intent1);
    }
}
