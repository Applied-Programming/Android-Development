package com.myapps.aniruddha.myattendance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Timetable extends ActionBarActivity implements View.OnClickListener {


    TextView t1,t2,t3,t4,t5;
    EditText ms1,ms2,ms3,ms4,ms5,ts1,ts2,ts3,ts4,ts5,ws1,ws2,ws3,ws4,ws5,ths1,ths2,ths3,ths4,ths5,fs1,fs2,fs3,fs4,fs5,ss1,ss2,ss3,ss4,ss5;
    Button bSavett;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

/*
        bSaveS = (Button) findViewById(R.id.bSaveS);
        bSaveS.setOnClickListener(this);
*/
        
        bSavett = (Button) findViewById(R.id.bSavett);
        bSavett.setOnClickListener(this);
        
        ms1 = (EditText) findViewById(R.id.ms1);
        ms2 = (EditText) findViewById(R.id.ms2);
        ms3 = (EditText) findViewById(R.id.ms3);
        ms4 = (EditText) findViewById(R.id.ms4);
        ms5 = (EditText) findViewById(R.id.ms5);
        ts1 = (EditText) findViewById(R.id.ts1);
        ts2 = (EditText) findViewById(R.id.ts2);
        ts3 = (EditText) findViewById(R.id.ts3);
        ts4 = (EditText) findViewById(R.id.ts4);
        ts5 = (EditText) findViewById(R.id.ts5);
        ws1 = (EditText) findViewById(R.id.ws1);
        ws2 = (EditText) findViewById(R.id.ws2);
        ws3 = (EditText) findViewById(R.id.ws3);
        ws4 = (EditText) findViewById(R.id.ws4);
        ws5 = (EditText) findViewById(R.id.ws5);
        ths1 = (EditText) findViewById(R.id.ths1);
        ths2 = (EditText) findViewById(R.id.ths2);
        ths3 = (EditText) findViewById(R.id.ths3);
        ths4 = (EditText) findViewById(R.id.ths4);
        ths5 = (EditText) findViewById(R.id.ths5);
        fs1 = (EditText) findViewById(R.id.fs1);
        fs2 = (EditText) findViewById(R.id.fs2);
        fs3 = (EditText) findViewById(R.id.fs3);
        fs4 = (EditText) findViewById(R.id.fs4);
        fs5 = (EditText) findViewById(R.id.fs5);
        ss1 = (EditText) findViewById(R.id.ss1);
        ss2 = (EditText) findViewById(R.id.ss2);
        ss3 = (EditText) findViewById(R.id.ss3);
        ss4 = (EditText) findViewById(R.id.ss4);
        ss5 = (EditText) findViewById(R.id.ss5);

        SharedPreferences sharedtt = getSharedPreferences("timetable", Context.MODE_PRIVATE);

        String s1m = sharedtt.getString("ms1", "");
        String s2m = sharedtt.getString("ms2", "");
        String s3m = sharedtt.getString("ms3", "");
        String s4m = sharedtt.getString("ms4", "");
        String s5m = sharedtt.getString("ms5", "");

        String s1t = sharedtt.getString("ts1", "");
        String s2t = sharedtt.getString("ts2", "");
        String s3t = sharedtt.getString("ts3", "");
        String s4t = sharedtt.getString("ts4", "");
        String s5t = sharedtt.getString("ts5", "");

        String s1w = sharedtt.getString("ws1", "");
        String s2w = sharedtt.getString("ws2", "");
        String s3w = sharedtt.getString("ws3", "");
        String s4w = sharedtt.getString("ws4", "");
        String s5w = sharedtt.getString("ws5", "");

        String s1th = sharedtt.getString("ths1", "");
        String s2th = sharedtt.getString("ths2", "");
        String s3th = sharedtt.getString("ths3", "");
        String s4th = sharedtt.getString("ths4", "");
        String s5th = sharedtt.getString("ths5", "");

        String s1f = sharedtt.getString("fs1", "");
        String s2f = sharedtt.getString("fs2", "");
        String s3f = sharedtt.getString("fs3", "");
        String s4f = sharedtt.getString("fs4", "");
        String s5f = sharedtt.getString("fs5", "");

        String s1s = sharedtt.getString("ss1", "");
        String s2s = sharedtt.getString("ss2", "");
        String s3s = sharedtt.getString("ss3", "");
        String s4s = sharedtt.getString("ss4", "");
        String s5s = sharedtt.getString("ss5", "");

        ms1.setText(s1m);
        ms2.setText(s2m);
        ms3.setText(s3m);
        ms4.setText(s4m);
        ms5.setText(s5m);

        ts1.setText(s1t);
        ts2.setText(s2t);
        ts3.setText(s3t);
        ts4.setText(s4t);
        ts5.setText(s5t);

        ws1.setText(s1w);
        ws2.setText(s2w);
        ws3.setText(s3w);
        ws4.setText(s4w);
        ws5.setText(s5w);

        ths1.setText(s1th);
        ths2.setText(s2th);
        ths3.setText(s3th);
        ths4.setText(s4th);
        ths5.setText(s5th);

        fs1.setText(s1f);
        fs2.setText(s2f);
        fs3.setText(s3f);
        fs4.setText(s4f);
        fs5.setText(s5f);

        ss1.setText(s1s);
        ss2.setText(s2s);
        ss3.setText(s3s);
        ss4.setText(s4s);
        ss5.setText(s5s);


        /*if (edittvs1.getText().toString()=="Subject1"){
            edittvs1.setTextColor(Color.argb(255,170,170,170));
        }
        if (edittvs2.getText().toString()=="Subject2"){
            edittvs2.setTextColor(Color.argb(255,170,170,170));
        }
        if (edittvs3.getText().toString()=="Subject3") {
            edittvs3.setTextColor(Color.argb(255, 170, 170, 170));
        }
        if (edittvs4.getText().toString()=="Subject4"){
            edittvs4.setTextColor(Color.argb(255,170,170,170));
        }
        if (edittvs5.getText().toString()=="Subject5"){
            edittvs5.setTextColor(Color.argb(255,170,170,170));
        }*/
        } catch (Exception e) {
            Dialog d = new Dialog(this);
            d.setTitle("Error E!");
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            d.setContentView(tv);
            d.show();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bSavett:

                try {

                    Intent i = new Intent(this, MainActivity.class);

                    String m1String = ms1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String m2String = ms2.getText().toString();
                    String m3String = ms3.getText().toString();
                    String m4String = ms4.getText().toString();
                    String m5String = ms5.getText().toString();

                    String t1String = ts1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String t2String = ts2.getText().toString();
                    String t3String = ts3.getText().toString();
                    String t4String = ts4.getText().toString();
                    String t5String = ts5.getText().toString();

                    String w1String = ws1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String w2String = ws2.getText().toString();
                    String w3String = ws3.getText().toString();
                    String w4String = ws4.getText().toString();
                    String w5String = ws5.getText().toString();

                    String th1String = ths1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String th2String = ths2.getText().toString();
                    String th3String = ths3.getText().toString();
                    String th4String = ths4.getText().toString();
                    String th5String = ths5.getText().toString();

                    String f1String = fs1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String f2String = fs2.getText().toString();
                    String f3String = fs3.getText().toString();
                    String f4String = fs4.getText().toString();
                    String f5String = fs5.getText().toString();

                    String s1String = ss1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String s2String = ss2.getText().toString();
                    String s3String = ss3.getText().toString();
                    String s4String = ss4.getText().toString();
                    String s5String = ss5.getText().toString();
                    
                    SharedPreferences sharedtt = getSharedPreferences("timetable", Context.MODE_PRIVATE);
                    SharedPreferences.Editor tteditor = sharedtt.edit();

                    tteditor.putString("ms1", m1String);
                    tteditor.putString("ms2", m2String);
                    tteditor.putString("ms3", m3String);
                    tteditor.putString("ms4", m4String);
                    tteditor.putString("ms5", m5String);

                    tteditor.putString("ts1", t1String);
                    tteditor.putString("ts2", t2String);
                    tteditor.putString("ts3", t3String);
                    tteditor.putString("ts4", t4String);
                    tteditor.putString("ts5", t5String);

                    tteditor.putString("ws1", w1String);
                    tteditor.putString("ws2", w2String);
                    tteditor.putString("ws3", w3String);
                    tteditor.putString("ws4", w4String);
                    tteditor.putString("ws5", w5String);

                    tteditor.putString("ths1", th1String);
                    tteditor.putString("ths2", th2String);
                    tteditor.putString("ths3", th3String);
                    tteditor.putString("ths4", th4String);
                    tteditor.putString("ths5", th5String);

                    tteditor.putString("fs1", f1String);
                    tteditor.putString("fs2", f2String);
                    tteditor.putString("fs3", f3String);
                    tteditor.putString("fs4", f4String);
                    tteditor.putString("fs5", f5String);

                    tteditor.putString("ss1", s1String);
                    tteditor.putString("ss2", s2String);
                    tteditor.putString("ss3", s3String);
                    tteditor.putString("ss4", s4String);
                    tteditor.putString("ss5", s5String);

                    tteditor.apply();

                    /*String s1m = sharedtt.getString("ms1", "Subject1");
                    String s2m = sharedtt.getString("ms2", "Subject2");
                    String s3m = sharedtt.getString("ms3", "Subject3");
                    String s4m = sharedtt.getString("ms4", "Subject4");
                    String s5m = sharedtt.getString("ms5", "Subject5");

                    String s1t = sharedtt.getString("ts1", "Subject1");
                    String s2t = sharedtt.getString("ts2", "Subject2");
                    String s3t = sharedtt.getString("ts3", "Subject3");
                    String s4t = sharedtt.getString("ts4", "Subject4");
                    String s5t = sharedtt.getString("ts5", "Subject5");

                    String s1w = sharedtt.getString("ws1", "Subject1");
                    String s2w = sharedtt.getString("ws2", "Subject2");
                    String s3w = sharedtt.getString("ws3", "Subject3");
                    String s4w = sharedtt.getString("ws4", "Subject4");
                    String s5w = sharedtt.getString("ws5", "Subject5");

                    String s1th = sharedtt.getString("ths1", "Subject1");
                    String s2th = sharedtt.getString("ths2", "Subject2");
                    String s3th = sharedtt.getString("ths3", "Subject3");
                    String s4th = sharedtt.getString("ths4", "Subject4");
                    String s5th = sharedtt.getString("ths5", "Subject5");

                    String s1f = sharedtt.getString("fs1", "Subject1");
                    String s2f = sharedtt.getString("fs2", "Subject2");
                    String s3f = sharedtt.getString("fs3", "Subject3");
                    String s4f = sharedtt.getString("fs4", "Subject4");
                    String s5f = sharedtt.getString("fs5", "Subject5");

                    String s1s = sharedtt.getString("ss1", "Subject1");
                    String s2s = sharedtt.getString("ss2", "Subject2");
                    String s3s = sharedtt.getString("ss3", "Subject3");
                    String s4s = sharedtt.getString("ss4", "Subject4");
                    String s5s = sharedtt.getString("ss5", "Subject5");*/

                    String s1m = sharedtt.getString("ms1", " ");
                    String s2m = sharedtt.getString("ms2", " ");
                    String s3m = sharedtt.getString("ms3", " ");
                    String s4m = sharedtt.getString("ms4", " ");
                    String s5m = sharedtt.getString("ms5", " ");

                    String s1t = sharedtt.getString("ts1", " ");
                    String s2t = sharedtt.getString("ts2", " ");
                    String s3t = sharedtt.getString("ts3", " ");
                    String s4t = sharedtt.getString("ts4", " ");
                    String s5t = sharedtt.getString("ts5", " ");

                    String s1w = sharedtt.getString("ws1", " ");
                    String s2w = sharedtt.getString("ws2", " ");
                    String s3w = sharedtt.getString("ws3", " ");
                    String s4w = sharedtt.getString("ws4", " ");
                    String s5w = sharedtt.getString("ws5", " ");

                    String s1th = sharedtt.getString("ths1", " ");
                    String s2th = sharedtt.getString("ths2", " ");
                    String s3th = sharedtt.getString("ths3", " ");
                    String s4th = sharedtt.getString("ths4", " ");
                    String s5th = sharedtt.getString("ths5", " ");

                    String s1f = sharedtt.getString("fs1", " ");
                    String s2f = sharedtt.getString("fs2", " ");
                    String s3f = sharedtt.getString("fs3", " ");
                    String s4f = sharedtt.getString("fs4", " ");
                    String s5f = sharedtt.getString("fs5", " ");

                    String s1s = sharedtt.getString("ss1", " ");
                    String s2s = sharedtt.getString("ss2", " ");
                    String s3s = sharedtt.getString("ss3", " ");
                    String s4s = sharedtt.getString("ss4", " ");
                    String s5s = sharedtt.getString("ss5", " ");

                    ms1.setText(s1m);
                    ms2.setText(s2m);
                    ms3.setText(s3m);
                    ms4.setText(s4m);
                    ms5.setText(s5m);

                    ts1.setText(s1t);
                    ts2.setText(s2t);
                    ts3.setText(s3t);
                    ts4.setText(s4t);
                    ts5.setText(s5t);

                    ws1.setText(s1w);
                    ws2.setText(s2w);
                    ws3.setText(s3w);
                    ws4.setText(s4w);
                    ws5.setText(s5w);

                    ths1.setText(s1th);
                    ths2.setText(s2th);
                    ths3.setText(s3th);
                    ths4.setText(s4th);
                    ths5.setText(s5th);

                    fs1.setText(s1f);
                    fs2.setText(s2f);
                    fs3.setText(s3f);
                    fs4.setText(s4f);
                    fs5.setText(s5f);

                    ss1.setText(s1s);
                    ss2.setText(s2s);
                    ss3.setText(s3s);
                    ss4.setText(s4s);
                    ss5.setText(s5s);

                    Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
                    startActivity(i);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error TT!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

        }
    }


}
