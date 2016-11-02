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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.Permission;
import java.security.Permissions;
import java.sql.Time;
import java.util.Calendar;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
{
    public static final String MyPREFERENCES = "MyPrefs";
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
        int alarmStatus = sharedPreferences.getInt("alarmStatus",1);
        String alarmLabel = sharedPreferences.getString("alarmLabel","Wake Up!");
        alarmText=hr+":"+min+" "+ampm;
        if(min <10 ){alarmText=hr+":0"+min+" "+ampm;}
        if(hr <10 ){alarmText="0"+hr+":"+min+" "+ampm;}
        if(min<10 && hr<10 ){alarmText="0"+hr+" : 0"+min+" "+ampm;}
        String at = alarmText;
        TextView tv = (TextView) findViewById(R.id.textView1);
        TextView tv1 = (TextView) findViewById(R.id.mainLabel);
        tv.setText(alarmText);
        tv1.setText(alarmLabel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("alarmLabel",alarmLabel);
        editor.putString("alarmText",at);
        editor.commit();
        Switch sw  = (Switch) findViewById(R.id.switch1);

        if(alarmStatus==1) {sw.setChecked(true);}
        else {sw.setChecked(false);}

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id== R.id.i1)
        {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

    public void switchAction(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch sw = (Switch)findViewById(R.id.switch1);
        if(sw.isChecked())
        {
           Toast toast =  Toast.makeText(getApplicationContext(),"Alarm On",Toast.LENGTH_SHORT);
            toast.show();
            editor.putInt("alarmStatus",1);
            editor.commit();
        }
        else
        {
            Toast toast =  Toast.makeText(getApplicationContext(),"Alarm off",Toast.LENGTH_SHORT);
            toast.show();
            editor.putInt("alarmStatus",0);
            editor.commit();
        }
    }
}
