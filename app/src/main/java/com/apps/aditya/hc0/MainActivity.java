package com.apps.aditya.hc0;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.security.Permission;
import java.security.Permissions;
import java.sql.Time;
import java.util.Calendar;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
{
    public static final String MyPREFERENCES = "MyPrefs" ;
    String alarmText="";
    String ampm="AM";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int min = sharedPreferences.getInt("ringMin",0);
        int hr = sharedPreferences.getInt("ringHour",0);
        ampm = sharedPreferences.getString("ampm","AM");
        alarmText=hr+" : "+min+" "+ampm;

        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(alarmText);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Do stuff here if you want - Aditya
            }
            else
            {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        }

    }

    public void launchFull(View view)
    {
        Intent intent = new Intent(this,FullscreenActivity.class);
        startActivity(intent);
    }

    public void launchPicker(View view)
    {
        Intent intent = new Intent(this,Picker.class);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 200:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                }
                else
                {
                    // permission denied, boo! Disable the
                }
                return;
            }
        }
    }
}
