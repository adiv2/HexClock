package com.apps.aditya.hc0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity
{
    MediaPlayer mp;
    PowerManager.WakeLock wl;
    public static final String MyPREFERENCES = "MyPrefs" ;
    int r,g,b;
    int mainC1=1,mainC2,mainC3,mainDt=2;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable()
    {
        @SuppressLint("InlinedApi")
        @Override
        public void run()
        {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable()
    {
        @Override
        public void run()
        {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                //actionBar.show();
                actionBar.hide();
            }
            mControlsView.setVisibility(View.INVISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            if (AUTO_HIDE)
            {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_fullscreen);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String alarmLabel = sharedPreferences.getString("alarmLabel","Wake Up!");
        TextView tv = (TextView) findViewById(R.id.dtext);
        tv.setText(alarmLabel);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY, "My Tag");
        wl.acquire();

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        Thread t = new Thread() {

            @Override
            public void run() {
                try {

                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                dj();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        play();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        delayedHide(100);
    }

    private void toggle()
    {
        if (mVisible)
        {
            hide();
        } else
        {
            show();
        }
    }

    private void hide()
    {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show()
    {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis)
    {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void radomizer()
    {
        r = (int)(Math.random()*255);
        b = (int)(Math.random()*255);
        g = (int)(Math.random()*255);
    }
    public void dj()
    {
        int ir = (int)(Math.random()*255);
        int ig = (int)(Math.random()*255);
        int ib = (int)(Math.random()*255);
        Color dTextColor =  new Color();
        int dText1 = dTextColor.argb(255,ir,ig,ib);
        radomizer();
        Color color1 = new Color();
        int col1 = color1.argb(255,r,g,b);
        radomizer();
        Color color2 = new Color();
        int col2 = color2.argb(255,r,g,b);
        radomizer();
        Color color3 = new Color();
        int col3 = color3.argb(255,r,g,b);
        radomizer();
        Color color4 = new Color();
        int col4 = color4.argb(255,r,g,b);
        radomizer();
        Color color5 = new Color();
        int col5 = color5.argb(255,r,g,b);
        int[] colors = {col1,col2,col3,col4,col5,dText1};
        int a,b,c,d,e,f;
        a = (int) (Math.random() * 4);
        b = (int) (Math.random() * 4);
        c = (int) (Math.random() * 4);
        d = (int) (Math.random() * 4);
        e = (int) (Math.random() * 4);
        f = (int) (Math.random() * 4);
        while(true)
        {
            if(a!=b && a!=c && a!=d && a!=e && a!=f && b!=c && b!=d && b!=e && b!=f && c!=d && c!=e && c!=f && d!=e && d!=f && e!=f)
            {break;}
            else
            {
                a = (int) (Math.random() * colors.length);
                b = (int) (Math.random() * colors.length);
                c = (int) (Math.random() * colors.length);
                d = (int) (Math.random() * colors.length);
                e = (int) (Math.random() * colors.length);
                f = (int) (Math.random() * colors.length);
            }
        }
        int h= Calendar.getInstance().get(Calendar.HOUR);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        int s = Calendar.getInstance().get(Calendar.SECOND);

        mainDt = dText1;
        Button hour = (Button) findViewById(R.id.hour);
        Drawable x = hour.getBackground();
        hour.setText(String.valueOf(h));
        hour.setBackgroundTintList(ColorStateList.valueOf(colors[a]));
        hour.animate();
        mainC1 = colors[a];

        Button min = (Button) findViewById(R.id.min);
        min.setText(String.valueOf(m));
        min.setBackgroundTintList(ColorStateList.valueOf(colors[b]));
        mainC2 = colors[b];

        Button sec = (Button) findViewById(R.id.sec);
        sec.setText(String.valueOf(s));
        sec.setBackgroundTintList(ColorStateList.valueOf(colors[c]));
        mainC3 = colors[b];

        TextView tv = (TextView) findViewById(R.id.dtext);
        tv.setTextColor(dText1);

    }

    public void exitApp1(View view)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
        if(mainDt==mainC1)
        {
            finish();
            System.exit(1);
            wl.release();
        }
        System.out.print("1");
    }
    public void exitApp2(View view)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
        if(mainDt==mainC2)
        {
            finish();
            System.exit(1);
            wl.release();
        }
        System.out.print("1");
    }
    public void exitApp3(View view)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
        if(mainDt==mainC3)
        {
            finish();
            System.exit(1);
            wl.release();
        }
        System.out.print("1");
    }

    public void play()
    {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String alarmTune = sharedpreferences.getString("tune","");
        int time = sharedpreferences.getInt("one",10);
        Log.d("Hello", "play: "+alarmTune+" "+time);
        if(alarmTune.equalsIgnoreCase("Soothing")){alarmTune="alarmTone1.mp3";}
        else{alarmTune="alarmTone2.mp3";}
        Vibrator v1 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long pattern[]={0,1000,1000};
        v1.vibrate(pattern,0);
        Log.d("Hello", "play: "+alarmTune+" "+time);
        MediaPlayer mp=new MediaPlayer();
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(alarmTune);
            mp.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());//Write your location here
            mp.prepare();
            mp.start();

        }catch(Exception e1){e1.printStackTrace();}
    }

}
