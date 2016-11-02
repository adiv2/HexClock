package com.apps.aditya.hc0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity
{
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String alarmLabel = sharedPreferences.getString("alarmLabel","Wake up!");
        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(alarmLabel);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tunes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int posx=0;
        if(sharedPreferences.getString("tune","10").equalsIgnoreCase("Inspiring"))
        {
            posx=1;
        }
        spinner.setSelection(posx,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Context context =parent.getContext();
                int duration = Toast.LENGTH_SHORT;
                String item = parent.getItemAtPosition(pos).toString();
                Toast toast = Toast.makeText(context, item, duration);
                toast.show();
                editor.putString("tune", item);
                editor.commit();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });

    }

    public void saveLabel(View view)
    {
        EditText et = (EditText) findViewById(R.id.editText);
        String alarmLabel= et.getText().toString();
        editor.putString("alarmLabel",alarmLabel);
        editor.commit();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
