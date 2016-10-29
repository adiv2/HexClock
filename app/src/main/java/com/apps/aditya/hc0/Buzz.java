package com.apps.aditya.hc0;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

public class Buzz extends AppCompatActivity
{
    int r,g,b;
    int mainC1=1,mainC2,mainC3,mainDt=2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dj();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
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
       // int dt = dTextColor.hashCode();
        //String dText1 = String.format("#%06X", (0xFFFFFF & dt));
       // dText.setText(dText1);
       // dText.setForeground(dTextColor);
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
        int h= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
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
       if(mainDt==mainC1)
        {
            finish();
            System.exit(1);
        }
        System.out.print("1");
    }
    public void exitApp2(View view)
    {
        if(mainDt==mainC2)
        {
            finish();
            System.exit(1);
        }
        System.out.print("1");
    }
    public void exitApp3(View view)
    {
        if(mainDt==mainC3)
        {
            finish();
            System.exit(1);
        }
        System.out.print("1");
    }

}
