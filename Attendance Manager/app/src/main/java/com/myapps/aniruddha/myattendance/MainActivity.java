package com.myapps.aniruddha.myattendance;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ApiUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.mariosangiorgio.ratemyapp.RateMyApp;
import com.mariosangiorgio.ratemyapp.RateMyAppBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, OnCheckedChangeListener {

    NotificationCompat.Builder notification0;
    private static final int uniqueID0 = 45610;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    NotificationCompat.Builder notification2;
    private static final int uniqueID2 = 45613;
    NotificationCompat.Builder notification3;
    private static final int uniqueID3 = 45614;
    NotificationCompat.Builder notification4;
    private static final int uniqueID4 = 45615;
    NotificationCompat.Builder notification5;
    private static final int uniqueID5 = 45616;

    int count1 = 0;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
    MyDBHandler dbHandler;
    StatsDB dbStatsHandler;
    Stats2DB dbStats2Handler;
    Stats3DB dbStats3Handler;
    Stats4DB dbStats4Handler;
    Stats5DB dbStats5Handler;
    /*DBC1 dbC1Handler;
    DBC2 dbC2Handler;
    DBC3 dbC3Handler;
    DBC4 dbC4Handler;
    DBC5 dbC5Handler;*/

    TabHost th;

    boolean bool1 = true, isNEclicked = false;

    TextView tvStatus;


    Button bDeleteRow, bView, bViewTT, bEditS, bClear, bClearStats, bUpdateStats, bs1p, bs1a, bs1n, bs2p, bs2a, bs2n, bs3p, bs3a, bs3n, bs4p, bs4a, bs4n, bs5p, bs5a, bs5n;
    TextView mtvs1, mtvs2, mtvs3, mtvs4, mtvs5, m2tvs1, m2tvs2, m2tvs3, m2tvs4, m2tvs5, m3tvs1, m3tvs2, m3tvs3, m3tvs4, m3tvs5;
    EditText ets1te, ets1ta, ets1p, ets2te, ets2ta, ets2p, ets3te, ets3ta, ets3p, ets4te, ets4ta, ets4p, ets5te, ets5ta, ets5p, etDelete;

    TextView tvDate, tvDay;
    String smonth, currentDate, dayOfTheWeek;
    Calendar calendar;
    public int mDay, mMonth, mYear, mDate, mDayOfWeek;

    String tvD, c1 = " --", c2 = " --", c3 = " --", c4 = " --", c5 = " --";
    //int toggle1,toggle2,toggle3,toggle4,toggle5;
    String dbString = "DBview";
    public int dbMonth, dbDate;

    TextView tvr1, tvr2, tvr3, tvr4, tvr5, tvr6, tvr7;

    Float minPer;

    int s1te = 0, s1ta = 0, s2te = 0, s2ta = 0, s3te = 0, s3ta = 0, s4te = 0, s4ta = 0, s5te = 0, s5ta = 0;
    //int s1te , s1ta , s2te , s2ta , s3te , s3ta , s4te , s4ta , s5te , s5ta ;
    double s1p = 0.0, s2p = 0.0, s3p = 0.0, s4p = 0.0, s5p = 0.0;

    String s1m = " ", s2m = " ", s3m = " ", s4m = " ", s5m = " ", s1t = " ", s2t = " ", s3t = " ", s4t = " ", s5t = " ", s1w = " ", s2w = " ", s3w = " ", s4w = " ", s5w = " ", s1th = " ", s2th = " ", s3th = " ", s4th = " ", s5th = " ", s1f = " ", s2f = " ", s3f = " ", s4f = " ", s5f = " ", s1s = " ", s2s = " ", s3s = " ", s4s = " ", s5s = " ";
    String s1n, s2n, s3n, s4n, s5n;

    public ShowcaseView showcaseView;
    private int counter = 0;
    private final ApiUtils apiUtils = new ApiUtils();

    public NotificationManager nm5;
    public NotificationManager nm4;
    public NotificationManager nm3;
    public NotificationManager nm2;
    public NotificationManager nm;
    public NotificationManager nm0;
    private boolean notifics = true;

    String ns1, ns2, ns3, ns4, ns5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            /*Target viewTarget = new ViewTarget(R.id.bEditS, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget)
                    .setContentTitle(R.string.title_single_shot)
                    .setContentText(R.string.R_string_desc_single_shot)
                    .singleShot(42)
                    .build();*/
            /*Target viewTarget = new ViewTarget(R.id.bEditS, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget)
                    .setContentTitle("Edit Subject Names")
                    .setContentText("Set your subject names and cutoff percentage.")
                    .singleShot(42)
                    .build();
            */


            showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(findViewById(R.id.bEditS)))
                    .setContentTitle("Edit Subject Names")
                    .setContentText("Set your subject names and cutoff percentage.")
                    .setStyle(R.style.CustomShowcaseTheme)
                    .singleShot(42)
                    .setOnClickListener(this)
                    .build();
            showcaseView.setButtonText(getString(R.string.next));


            //Set all the tabs
            th = (TabHost) findViewById(R.id.tabHost);
            th.setup();

            TabHost.TabSpec specs = th.newTabSpec("tag1");
            //Tab1
            specs.setContent(R.id.tab1);     //set content to tab3
            specs.setIndicator("Daily Ticker");      //name of the tab
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            th.addTab(specs);
            //Tab2
            specs = th.newTabSpec("tag2");
            //specs.setContent(R.layout.dbview);     //set content to tab2
            specs.setContent(R.id.tab2);     //set content to tab2
            specs.setIndicator("Register");      //name of the tab
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            th.addTab(specs);
            //Tab3
            specs = th.newTabSpec("tag3");
            specs.setContent(R.id.tab3);     //set content to tab3
            specs.setIndicator("Statistics");      //name of the tab
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            th.addTab(specs);
            /*//Tab4
            specs = th.newTabSpec("tag4");
            *//*LinearLayout l1= (LinearLayout)findViewById(R.id.tab4);
            l1.setLayoutParams(LinearLayoutCompat.LayoutParams.);*//*
            specs.setContent(R.id.tab4);     //set content to tab1
            specs.setIndicator("Time Table"); //name of the tab
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
            th.addTab(specs);*/

            etDelete = (EditText) findViewById(R.id.etDelete);

            ets1te = (EditText) findViewById(R.id.ets1te);
            ets1ta = (EditText) findViewById(R.id.ets1ta);
            ets1p = (EditText) findViewById(R.id.ets1p);
            ets2te = (EditText) findViewById(R.id.ets2te);
            ets2ta = (EditText) findViewById(R.id.ets2ta);
            ets2p = (EditText) findViewById(R.id.ets2p);
            ets3te = (EditText) findViewById(R.id.ets3te);
            ets3ta = (EditText) findViewById(R.id.ets3ta);
            ets3p = (EditText) findViewById(R.id.ets3p);
            ets4te = (EditText) findViewById(R.id.ets4te);
            ets4ta = (EditText) findViewById(R.id.ets4ta);
            ets4p = (EditText) findViewById(R.id.ets4p);
            ets5te = (EditText) findViewById(R.id.ets5te);
            ets5ta = (EditText) findViewById(R.id.ets5ta);
            ets5p = (EditText) findViewById(R.id.ets5p);

            mtvs1 = (TextView) findViewById(R.id.mtvs1);
            mtvs2 = (TextView) findViewById(R.id.mtvs2);
            mtvs3 = (TextView) findViewById(R.id.mtvs3);
            mtvs4 = (TextView) findViewById(R.id.mtvs4);
            mtvs5 = (TextView) findViewById(R.id.mtvs5);

            m2tvs1 = (TextView) findViewById(R.id.m2tvs1);
            m2tvs2 = (TextView) findViewById(R.id.m2tvs2);
            m2tvs3 = (TextView) findViewById(R.id.m2tvs3);
            m2tvs4 = (TextView) findViewById(R.id.m2tvs4);
            m2tvs5 = (TextView) findViewById(R.id.m2tvs5);

            m3tvs1 = (TextView) findViewById(R.id.m3tvs1);
            m3tvs2 = (TextView) findViewById(R.id.m3tvs2);
            m3tvs3 = (TextView) findViewById(R.id.m3tvs3);
            m3tvs4 = (TextView) findViewById(R.id.m3tvs4);
            m3tvs5 = (TextView) findViewById(R.id.m3tvs5);

            tvDate = (TextView) findViewById(R.id.tvDate);
            tvDay = (TextView) findViewById(R.id.tvDay);
            bEditS = (Button) findViewById(R.id.bEditS);
            bEditS.setOnClickListener(this);
            bViewTT = (Button) findViewById(R.id.bViewTT);
            bViewTT.setOnClickListener(this);
            bDeleteRow = (Button) findViewById(R.id.bDeleteRow);
            bDeleteRow.setOnClickListener(this);

            bView = (Button) findViewById(R.id.bView);
            bView.setOnClickListener(this);
            bClear = (Button) findViewById(R.id.bClear);
            bClear.setOnClickListener(this);
            bClearStats = (Button) findViewById(R.id.bClearStats);
            bClearStats.setOnClickListener(this);

            /*bUpdate = (Button) findViewById(R.id.bUpdate);
            bUpdate.setOnClickListener(this);*/
            bUpdateStats = (Button) findViewById(R.id.bUpdateStats);
            bUpdateStats.setOnClickListener(this);


            //tvSQLinfo = (TextView) findViewById(R.id.tvSQLinfo);
            tvStatus = (TextView) findViewById(R.id.tvStatus);

            radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
            radioGroup1.setOnCheckedChangeListener(this);
            radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
            radioGroup2.setOnCheckedChangeListener(this);
            radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
            radioGroup3.setOnCheckedChangeListener(this);
            radioGroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
            radioGroup4.setOnCheckedChangeListener(this);
            radioGroup5 = (RadioGroup) findViewById(R.id.radioGroup5);
            radioGroup5.setOnCheckedChangeListener(this);

            //int a = radioGroup1.getCheckedRadioButtonId();

            tvr1 = (TextView) findViewById(R.id.tvr1);
            tvr2 = (TextView) findViewById(R.id.tvr2);
            tvr3 = (TextView) findViewById(R.id.tvr3);
            tvr4 = (TextView) findViewById(R.id.tvr4);
            tvr5 = (TextView) findViewById(R.id.tvr5);
            tvr6 = (TextView) findViewById(R.id.tvr6);
            tvr7 = (TextView) findViewById(R.id.tvr7);

        /*tvC1 = (TextView) findViewById(R.id.tvC1);
        tvC2 = (TextView) findViewById(R.id.tvC2);
        tvC3 = (TextView) findViewById(R.id.tvC3);
        tvC4 = (TextView) findViewById(R.id.tvC4);
        tvC5 = (TextView) findViewById(R.id.tvC5);
        */

            calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            dbDate = mDay;
            dbMonth = mMonth;
            showDate(mYear, mMonth + 1, mDay);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date dt = new Date(mYear, mMonth, mDay - 1);
            dayOfTheWeek = sdf.format(dt);
            tvDay.setText(dayOfTheWeek);


            //Edit subject names
            SharedPreferences sharedPref = getSharedPreferences("subjectnames", Context.MODE_PRIVATE);

            s1n = sharedPref.getString("s1Name", "Subject1");
            s2n = sharedPref.getString("s2Name", "Subject2");
            s3n = sharedPref.getString("s3Name", "Subject3");
            s4n = sharedPref.getString("s4Name", "Subject4");
            s5n = sharedPref.getString("s5Name", "Subject5");

            mtvs1.setText(s1n);
            mtvs2.setText(s2n);
            mtvs3.setText(s3n);
            mtvs4.setText(s4n);
            mtvs5.setText(s5n);

            m3tvs1.setText(s1n);
            m3tvs2.setText(s2n);
            m3tvs3.setText(s3n);
            m3tvs4.setText(s4n);
            m3tvs5.setText(s5n);

            String s1n2 = sharedPref.getString("s1Name", "S1");
            String s2n2 = sharedPref.getString("s2Name", "S2");
            String s3n2 = sharedPref.getString("s3Name", "S3");
            String s4n2 = sharedPref.getString("s4Name", "S4");
            String s5n2 = sharedPref.getString("s5Name", "S5");

            m2tvs1.setText(s1n2);
            m2tvs2.setText(s2n2);
            m2tvs3.setText(s3n2);
            m2tvs4.setText(s4n2);
            m2tvs5.setText(s5n2);


            //get timetable sharedprefs
            SharedPreferences sharedtt = getSharedPreferences("timetable", Context.MODE_PRIVATE);

            s1m = sharedtt.getString("ms1", "Subject1");
            s2m = sharedtt.getString("ms2", "Subject2");
            s3m = sharedtt.getString("ms3", "Subject3");
            s4m = sharedtt.getString("ms4", "Subject4");
            s5m = sharedtt.getString("ms5", "Subject5");

            s1t = sharedtt.getString("ts1", "Subject1");
            s2t = sharedtt.getString("ts2", "Subject2");
            s3t = sharedtt.getString("ts3", "Subject3");
            s4t = sharedtt.getString("ts4", "Subject4");
            s5t = sharedtt.getString("ts5", "Subject5");

            s1w = sharedtt.getString("ws1", "Subject1");
            s2w = sharedtt.getString("ws2", "Subject2");
            s3w = sharedtt.getString("ws3", "Subject3");
            s4w = sharedtt.getString("ws4", "Subject4");
            s5w = sharedtt.getString("ws5", "Subject5");

            s1th = sharedtt.getString("ths1", "Subject1");
            s2th = sharedtt.getString("ths2", "Subject2");
            s3th = sharedtt.getString("ths3", "Subject3");
            s4th = sharedtt.getString("ths4", "Subject4");
            s5th = sharedtt.getString("ths5", "Subject5");

            s1f = sharedtt.getString("fs1", "Subject1");
            s2f = sharedtt.getString("fs2", "Subject2");
            s3f = sharedtt.getString("fs3", "Subject3");
            s4f = sharedtt.getString("fs4", "Subject4");
            s5f = sharedtt.getString("fs5", "Subject5");

            s1s = sharedtt.getString("ss1", "Subject1");
            s2s = sharedtt.getString("ss2", "Subject2");
            s3s = sharedtt.getString("ss3", "Subject3");
            s4s = sharedtt.getString("ss4", "Subject4");
            s5s = sharedtt.getString("ss5", "Subject5");

            /*radioGroup1.check(R.id.rb3);
            radioGroup2.check(R.id.r2b3);
            radioGroup3.check(R.id.r3b3);
            radioGroup4.check(R.id.r4b3);
            radioGroup5.check(R.id.r5b3);*/

            //setTTsubjects();


            //DBHandler
            dbHandler = new MyDBHandler(this, null, null, 1);
            printDatabase();

        /*dbC1Handler = new DBC1(this, null, null, 1);
        printColumn1Entries();
        dbC2Handler = new DBC2(this, null, null, 1);
        printColumn2Entries();
        dbC3Handler = new DBC3(this, null, null, 1);
        printColumn3Entries();
        dbC4Handler = new DBC4(this, null, null, 1);
        printColumn4Entries();
        dbC5Handler = new DBC5(this, null, null, 1);
        printColumn5Entries();*/

            dbStatsHandler = new StatsDB(this, null, null, 1);
            dbStats2Handler = new Stats2DB(this, null, null, 1);
            dbStats3Handler = new Stats3DB(this, null, null, 1);
            dbStats4Handler = new Stats4DB(this, null, null, 1);
            dbStats5Handler = new Stats5DB(this, null, null, 1);
            printStats();

            if (savedInstanceState == null) { // This null guard protects us from calling appLaunched on rotation.
                RateMyAppBuilder builder = new RateMyAppBuilder();
                builder.setLaunchesBeforeAlert(3);  // Optional
                builder.setDaysBeforeAlert(5);      // Optional
                builder.setEmailAddress("aniruddhastapas@gmail.com");   // Optional. It will enable two-phase rating request

                RateMyApp rateMyApp = builder.build(this);
                rateMyApp.appLaunched(this);

            }


            /*String et1 = ets1p.getText().toString();
            String et2 = ets2p.getText().toString();
            String et3 = ets3p.getText().toString();
            String et4 = ets4p.getText().toString();
            String et5 = ets5p.getText().toString();

            String ets1 = ets1te.getText().toString();
            String ets2 = ets2te.getText().toString();
            String ets3 = ets3te.getText().toString();
            String ets4 = ets4te.getText().toString();
            String ets5 = ets5te.getText().toString();


            SharedPreferences sharedets = getSharedPreferences("edittext", Context.MODE_PRIVATE);
            SharedPreferences.Editor etseditor = sharedets.edit();

            etseditor.putString("ets1", et1);
            etseditor.putString("ets2", et2);
            etseditor.putString("ets3", et3);
            etseditor.putString("ets4", et4);
            etseditor.putString("ets5", et5);
            etseditor.putString("etste1", ets1);
            etseditor.putString("etste2", ets2);
            etseditor.putString("etste3", ets3);
            etseditor.putString("etste4", ets4);
            etseditor.putString("etste5", ets5);

            etseditor.apply();*/


            /*SharedPreferences notifs = getSharedPreferences("notificsTF", Context.MODE_PRIVATE);
            String tof = notifs.getString("Tof", "True");*/

            /*SharedPreferences notifs = getSharedPreferences("notificsTF", Context.MODE_PRIVATE);
            Boolean tof = notifs.getBoolean("Tof" , true);*/


            //if (tof) {

/*            notification0 = new NotificationCompat.Builder(this);
            notification0.setAutoCancel(true);

            SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);
            Float minPer = sharedPer.getFloat("minp", 75);

            Double p1 = Double.parseDouble(ets1p.getText().toString());
            Double p2 = Double.parseDouble(ets2p.getText().toString());
            Double p3 = Double.parseDouble(ets3p.getText().toString());
            Double p4 = Double.parseDouble(ets4p.getText().toString());
            Double p5 = Double.parseDouble(ets5p.getText().toString());

            Double minimumPercentage = minPer.doubleValue();

            if((p5 < minimumPercentage) && (!(ets5te.getText().toString().equals("0")))){
                ns5 = "\nYour attendance in " + mtvs5.getText().toString() + " is " + String.valueOf(p5) + "%";
            }else{
                ns5 = "";
            }
            if((p4 < minimumPercentage) && (!(ets4te.getText().toString().equals("0")))){
                ns4 = "\nYour attendance in " + mtvs4.getText().toString() + " is " + String.valueOf(p4) + "%";
            }else{
                ns4 = "";
            }
            if((p3 < minimumPercentage) && (!(ets3te.getText().toString().equals("0")))){
                ns3 = "\nYour attendance in " + mtvs3.getText().toString() + " is " + String.valueOf(p3) + "%";
            }else{
                ns3 = "";
            }
            if((p2 < minimumPercentage) && (!(ets2te.getText().toString().equals("0")))){
                ns2 = "\nYour attendance in " + mtvs2.getText().toString() + " is " + String.valueOf(p2) + "%";
            }else{
                ns2 = "";
            }
            if((p1 < minimumPercentage) && (!(ets1te.getText().toString().equals("0")))){
                ns1 = "\nYour attendance in " + mtvs1.getText().toString() + " is " + String.valueOf(p1) + "%";
            }else{
                ns1 = "";
            }


            if (((p5 < minimumPercentage) && (!(ets5te.getText().toString().equals("0")))) ||
                ((p4 < minimumPercentage) && (!(ets4te.getText().toString().equals("0")))) ||
                ((p3 < minimumPercentage) && (!(ets3te.getText().toString().equals("0")))) ||
                ((p2 < minimumPercentage) && (!(ets2te.getText().toString().equals("0")))) ||
                ((p1 < minimumPercentage) && (!(ets1te.getText().toString().equals("0"))))) {

                //Build the notification
                notification0.setSmallIcon(R.mipmap.notify);
                notification0.setTicker("Have a look at your attendance!");
                notification0.setWhen(System.currentTimeMillis());
                notification0.setContentTitle("Low Attendance!");
                notification0.setContentText(ns1 + "/n"+ ns2 + ns3 + ns4 + ns5);
                notification0.setContentText(ns2);
                notification0.setAutoCancel(true);
                    *//*Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notification0.setContentIntent(pendingIntent);*//*



                //Builds notifications and issues it
                nm0 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm0.notify(uniqueID0, notification0.build());


            }
                */

            notification = new NotificationCompat.Builder(this);
            notification.setAutoCancel(true);
            notification.build().flags |= Notification.FLAG_AUTO_CANCEL;
            notification2 = new NotificationCompat.Builder(this);
            notification2.setAutoCancel(true);
            notification3 = new NotificationCompat.Builder(this);
            notification3.setAutoCancel(true);
            notification4 = new NotificationCompat.Builder(this);
            notification4.setAutoCancel(true);
            notification5 = new NotificationCompat.Builder(this);
            notification5.setAutoCancel(true);


            SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);
            minPer = sharedPer.getFloat("minp", 75);

            Double p1 = Double.parseDouble(ets1p.getText().toString());
            Double p2 = Double.parseDouble(ets2p.getText().toString());
            Double p3 = Double.parseDouble(ets3p.getText().toString());
            Double p4 = Double.parseDouble(ets4p.getText().toString());
            Double p5 = Double.parseDouble(ets5p.getText().toString());

            Double minimumPercentage = minPer.doubleValue();

            if ((p5 < minimumPercentage) && (!(ets5te.getText().toString().equals("0")))) {
                //Build the notification
                notification5.setSmallIcon(R.mipmap.notify);
                notification5.setTicker("Have a look at your attendance!");
                notification5.setWhen(System.currentTimeMillis());
                notification5.setContentTitle("Low Attendance!");
                notification5.setContentText("Your attendance in " + mtvs5.getText().toString() + " is " + String.valueOf(p5) + "%");
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
                notification5.setContentIntent(pendingIntent);


                //Builds notifications and issues it
                nm5 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm5.notify(uniqueID5, notification5.build());


            }
            if ((p4 < minimumPercentage) && (!(ets4te.getText().toString().equals("0")))) {
                //Build the notification
                notification4.setSmallIcon(R.mipmap.notify);
                notification4.setTicker("Have a look at your attendance!");
                notification4.setWhen(System.currentTimeMillis());
                notification4.setContentTitle("Low Attendance!");
                notification4.setContentText("Your attendance in " + mtvs4.getText().toString() + " is " + String.valueOf(p4) + "%");
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification4.setContentIntent(pendingIntent);

                //Builds notifications and issues it
                nm4 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm4.notify(uniqueID4, notification4.build());
            }
            if ((p3 < minimumPercentage) && (!(ets3te.getText().toString().equals("0")))) {
                //Build the notification
                notification3.setSmallIcon(R.mipmap.notify);
                notification3.setTicker("Have a look at your attendance!");
                notification3.setWhen(System.currentTimeMillis());
                notification3.setContentTitle("Low Attendance!");
                notification3.setContentText("Your attendance in " + mtvs3.getText().toString() + " is " + String.valueOf(p3) + "%");
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification3.setContentIntent(pendingIntent);

                //Builds notifications and issues it
                nm3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm3.notify(uniqueID3, notification3.build());
            }
            if ((p2 < minimumPercentage) && (!(ets2te.getText().toString().equals("0")))) {
                //Build the notification
                notification2.setSmallIcon(R.mipmap.notify);
                notification2.setTicker("Have a look at your attendance!");
                notification2.setWhen(System.currentTimeMillis());
                notification2.setContentTitle("Low Attendance!");
                notification2.setContentText("Your attendance in " + mtvs2.getText().toString() + " is " + String.valueOf(p2) + "%");
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification2.setContentIntent(pendingIntent);

                //Builds notifications and issues it
                nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm2.notify(uniqueID2, notification2.build());
            }
            if ((p1 < minimumPercentage) && (!(ets1te.getText().toString().equals("0")))) {
                //Build the notification
                notification.setSmallIcon(R.mipmap.notify);
                notification.setTicker("Have a look at your attendance!");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Low Attendance!");
                notification.setContentText("Your attendance in " + mtvs1.getText().toString() + " is " + String.valueOf(p1) + "%");
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
                //notification.setDefaults(Notification.DEFAULT_ALL);
                //Builds notifications and issues it
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //notification.setAutoCancel(true);
                //notification.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                //notification.build().flags |= Notification.FLAG_AUTO_CANCEL;
                nm.notify(uniqueID, notification.build());
                //nm.cancel(uniqueID);

                //notification.setDefaults(Notification.DEFAULT_ALL);
            }
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            /*notification.build().flags |= PendingIntent.FLAG_CANCEL_CURRENT;
            notification2.build().flags |= PendingIntent.FLAG_CANCEL_CURRENT;
            notification3.build().flags |= PendingIntent.FLAG_CANCEL_CURRENT;
            notification4.build().flags |= PendingIntent.FLAG_CANCEL_CURRENT;
            notification5.build().flags |= PendingIntent.FLAG_CANCEL_CURRENT;*/

            //}


        } catch (Exception e) {
            Dialog d = new Dialog(this);
            d.setTitle("Error E!");
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            d.setContentView(tv);
            d.show();
        }

    }


    //ShowCaseView
    private void setAlpha(float alpha, View... views) {
        if (apiUtils.isCompatWithHoneycomb()) {
            for (View view : views) {
                view.setScrollContainer(true);
                view.setAlpha(alpha);
            }
        }
        /*if (apiUtils.isCompatWith(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
            for (View view : views) {
                view.setScrollContainer(true);
                view.setAlpha(alpha);
            }
        }
        if (apiUtils.isCompatWith(Build.VERSION_CODES.JELLY_BEAN)) {
            for (View view : views) {
                view.setScrollContainer(true);
                view.setAlpha(alpha);
            }
        }
        if (apiUtils.isCompatWith(Build.VERSION_CODES.KITKAT)) {
            for (View view : views) {
                view.setScrollContainer(true);
                view.setAlpha(alpha);
            }
        }
        if (apiUtils.isCompatWith(Build.VERSION_CODES.LOLLIPOP)) {
            for (View view : views) {
                view.setScrollContainer(true);
                view.setAlpha(alpha);
            }
        }*/
    }

    /*Target viewTarget2 = new ViewTarget(R.id.bViewTT, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget2)
                    .setContentTitle("Timetable")
                    .setContentText("Edit and view your timetable.")
                    .singleShot(42)
                    .build();

            Target viewTarget3 = new ViewTarget(R.id.radioGroup1, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget3)
                    .setContentTitle("Daily Ticker")
                    .setContentText("Daily set whether you were present/absent or the class wasn't engaged.")
                    .singleShot(42)
                    .build();

            Target viewTarget4 = new ViewTarget(R.id.bView, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget4)
                    .setContentTitle("Update")
                    .setContentText("After choosing, Click Update to update the Register and Statistics")
                    .singleShot(42)
                    .build();*/


    private void setTTsubjects() {
        radioGroup1.check(R.id.rb3);
        radioGroup2.check(R.id.r2b3);
        radioGroup3.check(R.id.r3b3);
        radioGroup4.check(R.id.r4b3);
        radioGroup5.check(R.id.r5b3);
        if (tvDay.getText().toString().equals("Monday")) {

            if (s1m.equals(s1n) || s2m.equals(s1n) || s3m.equals(s1n) || s4m.equals(s1n) || s5m.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1m.equals(s2n) || s2m.equals(s2n) || s3m.equals(s2n) || s4m.equals(s2n) || s5m.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1m.equals(s3n) || s2m.equals(s3n) || s3m.equals(s3n) || s4m.equals(s3n) || s5m.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1m.equals(s4n) || s2m.equals(s4n) || s3m.equals(s4n) || s4m.equals(s4n) || s5m.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1m.equals(s5n) || s2m.equals(s5n) || s3m.equals(s5n) || s4m.equals(s5n) || s5m.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        } else if (tvDay.getText().toString().equals("Tuesday")) {

            if (s1t.equals(s1n) || s2t.equals(s1n) || s3t.equals(s1n) || s4t.equals(s1n) || s5t.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1t.equals(s2n) || s2t.equals(s2n) || s3t.equals(s2n) || s4t.equals(s2n) || s5t.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1t.equals(s3n) || s2t.equals(s3n) || s3t.equals(s3n) || s4t.equals(s3n) || s5t.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1t.equals(s4n) || s2t.equals(s4n) || s3t.equals(s4n) || s4t.equals(s4n) || s5t.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1t.equals(s5n) || s2t.equals(s5n) || s3t.equals(s5n) || s4t.equals(s5n) || s5t.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        } else if (tvDay.getText().toString().equals("Wednesday")) {

            if (s1w.equals(s1n) || s2w.equals(s1n) || s3w.equals(s1n) || s4w.equals(s1n) || s5w.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1w.equals(s2n) || s2w.equals(s2n) || s3w.equals(s2n) || s4w.equals(s2n) || s5w.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1w.equals(s3n) || s2w.equals(s3n) || s3w.equals(s3n) || s4w.equals(s3n) || s5w.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1w.equals(s4n) || s2w.equals(s4n) || s3w.equals(s4n) || s4w.equals(s4n) || s5w.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1w.equals(s5n) || s2w.equals(s5n) || s3w.equals(s5n) || s4w.equals(s5n) || s5w.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        } else if (tvDay.getText().toString().equals("Thursday")) {

            if (s1th.equals(s1n) || s2th.equals(s1n) || s3th.equals(s1n) || s4th.equals(s1n) || s5th.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1th.equals(s2n) || s2th.equals(s2n) || s3th.equals(s2n) || s4th.equals(s2n) || s5th.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1th.equals(s3n) || s2th.equals(s3n) || s3th.equals(s3n) || s4th.equals(s3n) || s5th.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1th.equals(s4n) || s2th.equals(s4n) || s3th.equals(s4n) || s4th.equals(s4n) || s5th.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1th.equals(s5n) || s2th.equals(s5n) || s3th.equals(s5n) || s4th.equals(s5n) || s5th.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        } else if (tvDay.getText().toString().equals("Friday")) {

            if (s1f.equals(s1n) || s2f.equals(s1n) || s3f.equals(s1n) || s4f.equals(s1n) || s5f.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1f.equals(s2n) || s2f.equals(s2n) || s3f.equals(s2n) || s4f.equals(s2n) || s5f.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1f.equals(s3n) || s2f.equals(s3n) || s3f.equals(s3n) || s4f.equals(s3n) || s5f.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1f.equals(s4n) || s2f.equals(s4n) || s3f.equals(s4n) || s4f.equals(s4n) || s5f.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1f.equals(s5n) || s2f.equals(s5n) || s3f.equals(s5n) || s4f.equals(s5n) || s5f.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        } else if (tvDay.getText().toString().equals("Saturday")) {

            if (s1s.equals(s1n) || s2s.equals(s1n) || s3s.equals(s1n) || s4s.equals(s1n) || s5s.equals(s1n)) {
                radioGroup1.check(R.id.rb1);
            }
            if (s1s.equals(s2n) || s2s.equals(s2n) || s3s.equals(s2n) || s4s.equals(s2n) || s5s.equals(s2n)) {
                radioGroup2.check(R.id.r2b1);
            }
            if (s1s.equals(s3n) || s2s.equals(s3n) || s3s.equals(s3n) || s4s.equals(s3n) || s5s.equals(s3n)) {
                radioGroup3.check(R.id.r3b1);
            }
            if (s1s.equals(s4n) || s2s.equals(s4n) || s3s.equals(s4n) || s4s.equals(s4n) || s5s.equals(s4n)) {
                radioGroup4.check(R.id.r4b1);
            }
            if (s1s.equals(s5n) || s2s.equals(s5n) || s3s.equals(s5n) || s4s.equals(s5n) || s5s.equals(s5n)) {
                radioGroup5.check(R.id.r5b1);
            }
        }
        /*else{
            radioGroup1.check(R.id.rb3);
            radioGroup2.check(R.id.r2b3);
            radioGroup3.check(R.id.r3b3);
            radioGroup4.check(R.id.r4b3);
            radioGroup5.check(R.id.r5b3);
        }*/
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(this, "Set the Date!", Toast.LENGTH_SHORT).show();
    }

    private void showDate(int mYear, int mMonth, int mDay) {
        switch (mMonth) {
            case 1:
                smonth = "January";
                break;
            case 2:
                smonth = "February";
                break;
            case 3:
                smonth = "March";
                break;
            case 4:
                smonth = "April";
                break;
            case 5:
                smonth = "May";
                break;
            case 6:
                smonth = "June";
                break;
            case 7:
                smonth = "July";
                break;
            case 8:
                smonth = "August";
                break;
            case 9:
                smonth = "September";
                break;
            case 10:
                smonth = "October";
                break;
            case 11:
                smonth = "November";
                break;
            case 12:
                smonth = "December";
                break;

        }
        currentDate = String.valueOf(new StringBuilder().append(smonth).append(" ").append(mDay).append(",").append(mYear).append(" "));
        tvDate.setText(currentDate);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 999:
                return new DatePickerDialog(this, mDatesetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDatesetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            /*mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            tvDay.setText(mDate);*/
            showDate(year, monthOfYear + 1, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date(year, monthOfYear, dayOfMonth - 1);
            dayOfTheWeek = sdf.format(d);
            tvDay.setText(dayOfTheWeek);
            dbMonth = monthOfYear + 1;
            dbDate = dayOfMonth;
            //setTTsubjects();
        }
    };


    //Action Listener for Radio Group
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //tvD = dbDate + "/" + dbMonth;
        switch (checkedId) {

            case R.id.rb1:
                try {
                    c1 = "  P";

                    /*Products product = new Products("P");
                    dbC1Handler.addEntries(product);*/

                    if (ets1te.getText().toString().isEmpty()) {
                        s1te = 0;
                    } else {
                        s1te = Integer.parseInt(ets1te.getText().toString());
                    }
                    s1te++;

                    if (ets1ta.getText().toString().isEmpty()) {
                        s1ta = 0;
                    } else {
                        s1ta = Integer.parseInt(ets1ta.getText().toString());
                    }
                    s1ta++;
                    isNEclicked = false;

                    updateStats(s1te, s1ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 1.1!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                //printColumnEntries();
                /*updateDB udb = new updateDB(tvD,c1,c2,c3,c4,c5);
                dbHandler.addProduct(udb);
                bView.setText("Done!");*/
                //      printDatabase();
                /*setData = "  P";
                Products product = new Products("P");
                dbHandler.addProduct(tvD,product);
                printDatabase();*/
                break;

            case R.id.rb2:
                try {
                    c1 = "  A";

                    if (ets1te.getText().toString().isEmpty()) {
                        s1te = 0;
                    } else {
                        s1te = Integer.parseInt(ets1te.getText().toString());
                    }
                    s1te++;
                    if (ets1ta.getText().toString().isEmpty()) {
                        s1ta = 0;
                    } else {
                        s1ta = Integer.parseInt(ets1ta.getText().toString());
                    }
                    isNEclicked = false;

                    updateStats(s1te, s1ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 1.2!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.rb3:
                try {
                    c1 = " --";
                    if (ets1te.getText().toString().isEmpty()) {
                        s1te = 0;
                    } else {
                        s1te = Integer.parseInt(ets1te.getText().toString());
                    }
                    if (ets1ta.getText().toString().isEmpty()) {
                        s1ta = 0;
                    } else {
                        s1ta = Integer.parseInt(ets1ta.getText().toString());
                    }
                    if (s1te == 0) {
                        isNEclicked = true;
                    }

                    updateStats(s1te, s1ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 1.3!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r2b1:
                try {
                    c2 = "  P";

                    if (ets2te.getText().toString().isEmpty()) {
                        s2te = 0;
                    } else {
                        s2te = Integer.parseInt(ets2te.getText().toString());
                    }
                    s2te++;
                    if (ets2ta.getText().toString().isEmpty()) {
                        s2ta = 0;
                    } else {
                        s2ta = Integer.parseInt(ets2ta.getText().toString());
                    }
                    s2ta++;
                    isNEclicked = false;

                    updateStats2(s2te, s2ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 2.1!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r2b2:
                try {
                    c2 = "  A";

                    if (ets2te.getText().toString().isEmpty()) {
                        s2te = 0;
                    } else {
                        s2te = Integer.parseInt(ets2te.getText().toString());
                    }
                    s2te++;
                    if (ets2ta.getText().toString().isEmpty()) {
                        s2ta = 0;
                    } else {
                        s2ta = Integer.parseInt(ets2ta.getText().toString());
                    }
                    isNEclicked = false;

                    updateStats2(s2te, s2ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 2.2!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r2b3:
                try {
                    c2 = " --";

                    if (ets2te.getText().toString().isEmpty()) {
                        s2te = 0;
                    } else {
                        s2te = Integer.parseInt(ets2te.getText().toString());
                    }
                    if (ets2ta.getText().toString().isEmpty()) {
                        s2ta = 0;
                    } else {
                        s2ta = Integer.parseInt(ets2ta.getText().toString());
                    }
                    if (s2te == 0) {
                        isNEclicked = true;
                    }

                    updateStats2(s2te, s2ta);


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 2.3!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r3b1:
                try {
                    c3 = "  P";

                    if (ets3te.getText().toString().isEmpty()) {
                        s3te = 0;
                    } else {
                        s3te = Integer.parseInt(ets3te.getText().toString());
                    }
                    s3te++;
                    if (ets3ta.getText().toString().isEmpty()) {
                        s3ta = 0;
                    } else {
                        s3ta = Integer.parseInt(ets3ta.getText().toString());
                    }
                    s3ta++;
                    isNEclicked = false;

                    updateStats3(s3te, s3ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 3.1!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r3b2:
                try {
                    c3 = "  A";

                    if (ets3te.getText().toString().isEmpty()) {
                        s3te = 0;
                    } else {
                        s3te = Integer.parseInt(ets3te.getText().toString());
                    }
                    s3te++;
                    if (ets3ta.getText().toString().isEmpty()) {
                        s3ta = 0;
                    } else {
                        s3ta = Integer.parseInt(ets3ta.getText().toString());
                    }

                    updateStats3(s3te, s3ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 3.2!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r3b3:
                try {
                    c3 = " --";
                    if (ets3te.getText().toString().isEmpty()) {
                        s3te = 0;
                    } else {
                        s3te = Integer.parseInt(ets3te.getText().toString());
                    }
                    if (ets3ta.getText().toString().isEmpty()) {
                        s3ta = 0;
                    } else {
                        s3ta = Integer.parseInt(ets3ta.getText().toString());
                    }
                    if (s3te == 0) {
                        isNEclicked = true;
                    }

                    updateStats3(s3te, s3ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 3.3!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r4b1:
                try {
                    c4 = "  P";

                    if (ets4te.getText().toString().isEmpty()) {
                        s4te = 0;
                    } else {
                        s4te = Integer.parseInt(ets4te.getText().toString());
                    }
                    s4te++;
                    if (ets4ta.getText().toString().isEmpty()) {
                        s4ta = 0;
                    } else {
                        s4ta = Integer.parseInt(ets4ta.getText().toString());
                    }
                    s4ta++;
                    isNEclicked = false;

                    updateStats4(s4te, s4ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 4.1!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r4b2:
                try {
                    c4 = "  A";

                    if (ets4te.getText().toString().isEmpty()) {
                        s4te = 0;
                    } else {
                        s4te = Integer.parseInt(ets4te.getText().toString());
                    }
                    s4te++;
                    if (ets4ta.getText().toString().isEmpty()) {
                        s4ta = 0;
                    } else {
                        s4ta = Integer.parseInt(ets4ta.getText().toString());
                    }
                    isNEclicked = false;

                    updateStats4(s4te, s4ta);


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 4.2!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r4b3:
                try {
                    c4 = " --";
                    if (ets4te.getText().toString().isEmpty()) {
                        s4te = 0;
                    } else {
                        s4te = Integer.parseInt(ets4te.getText().toString());
                    }
                    if (ets4ta.getText().toString().isEmpty()) {
                        s4ta = 0;
                    } else {
                        s4ta = Integer.parseInt(ets4ta.getText().toString());
                    }
                    if (s4te == 0) {
                        isNEclicked = true;
                    }

                    updateStats4(s4te, s4ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 4.3!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r5b1:
                try {
                    c5 = "  P";

                    if (ets5te.getText().toString().isEmpty()) {
                        s5te = 0;
                    } else {
                        s5te = Integer.parseInt(ets5te.getText().toString());
                    }
                    s5te++;
                    if (ets5ta.getText().toString().isEmpty()) {
                        s5ta = 0;
                    } else {
                        s5ta = Integer.parseInt(ets5ta.getText().toString());
                    }
                    s5ta++;
                    isNEclicked = false;

                    updateStats5(s5te, s5ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 5.1!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r5b2:
                try {
                    c5 = "  A";

                    if (ets5te.getText().toString().isEmpty()) {
                        s5te = 0;
                    } else {
                        s5te = Integer.parseInt(ets5te.getText().toString());
                    }
                    s5te++;
                    if (ets5ta.getText().toString().isEmpty()) {
                        s5ta = 0;
                    } else {
                        s5ta = Integer.parseInt(ets5ta.getText().toString());
                    }
                    isNEclicked = false;

                    updateStats5(s5te, s5ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 5.2!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.r5b3:
                try {
                    c5 = " --";
                    if (ets5te.getText().toString().isEmpty()) {
                        s5te = 0;
                    } else {
                        s5te = Integer.parseInt(ets5te.getText().toString());
                    }
                    if (ets5ta.getText().toString().isEmpty()) {
                        s5ta = 0;
                    } else {
                        s5ta = Integer.parseInt(ets5ta.getText().toString());
                    }
                    if (s5te == 0) {
                        isNEclicked = true;
                    }

                    updateStats5(s5te, s5ta);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error 5.3!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;
            //default:
        }

        /*tvD = tvDate.getText().toString();
        //updateDB udb = new updateDB(tvD,c1,c2);
        updateDB udb = new updateDB();
        udb.set_date(tvD);
        udb.set_c1(c1);
        udb.set_c2(c2);*/

    }

    private void updateStats(int s1te, int s1ta) {
        if (!isNEclicked) {
            s1p = (double) s1ta / (double) s1te * 100;
        } else {
            s1p = 0.0;
        }
        UpdateStats stat = new UpdateStats(String.valueOf(s1te), String.valueOf(s1ta), String.valueOf(s1p));
        dbStatsHandler.addProduct(stat);
        isNEclicked = false;
    }

    private void updateStats2(int s2te, int s2ta) {
        if (!isNEclicked) {
            s2p = (double) s2ta / (double) s2te * 100;
        } else {
            s2p = 0.0;
        }
        UpdateStats2 stat2 = new UpdateStats2(String.valueOf(s2te), String.valueOf(s2ta), String.valueOf(s2p));
        dbStats2Handler.addProduct2(stat2);
        isNEclicked = false;
    }

    private void updateStats3(int s3te, int s3ta) {
        if (!isNEclicked) {
            s3p = (double) s3ta / (double) s3te * 100;
        } else {
            s3p = 0.0;
        }
        UpdateStats3 stat3 = new UpdateStats3(String.valueOf(s3te), String.valueOf(s3ta), String.valueOf(s3p));
        dbStats3Handler.addProduct3(stat3);
        isNEclicked = false;
    }

    private void updateStats4(int s4te, int s4ta) {
        if (!isNEclicked) {
            s4p = (double) s4ta / (double) s4te * 100;
        } else {
            s4p = 0.0;
        }
        UpdateStats4 stat4 = new UpdateStats4(String.valueOf(s4te), String.valueOf(s4ta), String.valueOf(s4p));
        dbStats4Handler.addProduct4(stat4);
        isNEclicked = false;
    }

    private void updateStats5(int s5te, int s5ta) {
        if (!isNEclicked) {
            s5p = (double) s5ta / (double) s5te * 100;
        } else {
            s5p = 0.0;
        }
        UpdateStats5 stat5 = new UpdateStats5(String.valueOf(s5te), String.valueOf(s5ta), String.valueOf(s5p));
        dbStats5Handler.addProduct5(stat5);
        isNEclicked = false;
    }

    private void printStats() {
        String dbteString = dbStatsHandler.databaseteStats();
        ets1te.setText(dbteString);
        String dbtaString = dbStatsHandler.databasetaStats();
        ets1ta.setText(dbtaString);
        String dbpString = dbStatsHandler.databasepStats();
        ets1p.setText(dbpString);
        String dbte2String = dbStats2Handler.databasete2Stats();
        ets2te.setText(dbte2String);
        String dbta2String = dbStats2Handler.databaseta2Stats();
        ets2ta.setText(dbta2String);
        String dbp2String = dbStats2Handler.databasep2Stats();
        ets2p.setText(dbp2String);
        String dbte3String = dbStats3Handler.databasete3Stats();
        ets3te.setText(dbte3String);
        String dbta3String = dbStats3Handler.databaseta3Stats();
        ets3ta.setText(dbta3String);
        String dbp3String = dbStats3Handler.databasep3Stats();
        ets3p.setText(dbp3String);
        String dbte4String = dbStats4Handler.databasete4Stats();
        ets4te.setText(dbte4String);
        String dbta4String = dbStats4Handler.databaseta4Stats();
        ets4ta.setText(dbta4String);
        String dbp4String = dbStats4Handler.databasep4Stats();
        ets4p.setText(dbp4String);
        String dbte5String = dbStats5Handler.databasete5Stats();
        ets5te.setText(dbte5String);
        String dbta5String = dbStats5Handler.databaseta5Stats();
        ets5ta.setText(dbta5String);
        String dbp5String = dbStats5Handler.databasep5Stats();
        ets5p.setText(dbp5String);
    }

    /*
        //print the column1 db
        private void printColumn1Entries() {
            String dbC1String = dbC1Handler.printColumn(); //call the databaseToString() metod from MyDBHandler class
            tvC1.setText(dbC1String);
        }
        //print the column2 db
        private void printColumn2Entries() {
            String dbC2String = dbC2Handler.printColumn(); //call the databaseToString() metod from MyDBHandler class
            tvC2.setText(dbC2String);
        }
        //print the column3 db
        private void printColumn3Entries() {
            String dbC3String = dbC3Handler.printColumn(); //call the databaseToString() metod from MyDBHandler class
            tvC3.setText(dbC3String);
        }
        //print the column4 db
        private void printColumn4Entries() {
            String dbC4String = dbC4Handler.printColumn(); //call the databaseToString() metod from MyDBHandler class
            tvC4.setText(dbC4String);
        }//print the column5 db
        private void printColumn5Entries() {
            String dbC5String = dbC5Handler.printColumn(); //call the databaseToString() metod from MyDBHandler class
            tvC5.setText(dbC5String);
        }
    */
    //print the db
    private void printDatabase() {
        dbString = dbHandler.databaseToString(); //call the databaseToString() metod from MyDBHandler class
        tvStatus.setText(dbString);
        /*String tempdbString = dbString;
        int indexa = tempdbString.indexOf(" ");
        int index1 = tempdbString.indexOf(" ",indexa);
        String srno = tempdbString.substring(0,index1);
        int index2 = tempdbString.indexOf(" ",index1);
        String regdate = tempdbString.substring(index1, index2);
        int index3 = tempdbString.indexOf(" ",index2);
        String s1s = tempdbString.substring(index2,index3);
        int index4 = tempdbString.indexOf(" ",index3);
        String s2s = tempdbString.substring(index3,index4);
        int index5 = tempdbString.indexOf(" ",index4);
        String s3s = tempdbString.substring(index4,index5);
        int index6 = tempdbString.indexOf(" ",index5);
        String s4s = tempdbString.substring(index5,index6);
        int index7 = tempdbString.indexOf(" ",index6);
        String s5s = tempdbString.substring(index6,index7);
        tvr1.setText(srno);
        tvr2.setText(regdate);
        tvr3.setText(s1s);
        tvr4.setText(s2s);
        tvr5.setText(s3s);
        tvr6.setText(s4s);
        tvr7.setText(s5s);
*/
        /*while(tempdbString.contains("\n")) {
            int indexn = tempdbString.indexOf("\n",0);
            String nString = tempdbString.substring(indexn);

            TextView tv1 = null, tv2 = null, tv3 = null, tv4 = null, tv5  = null, tv6 = null, tv7 = null;
            LinearLayout l1 = new LinearLayout(this);
            l1.setWeightSum(70);
            l1.setOrientation(LinearLayout.HORIZONTAL);

            tv1.setId(Integer.parseInt("1"));
            tv2.setId(Integer.parseInt("2"));
            tv3.setId(Integer.parseInt("3"));
            tv4.setId(Integer.parseInt("4"));
            tv5.setId(Integer.parseInt("5"));
            tv6.setId(Integer.parseInt("6"));
            tv7.setId(Integer.parseInt("7"));
            l1.addView(tv1);
            l1.addView(tv2);
            l1.addView(tv3);
            l1.addView(tv4);
            l1.addView(tv5);
            l1.addView(tv6);
            l1.addView(tv7);

            int indexn1 = nString.indexOf(" ");
            String srno2 = nString.substring(0,indexn1);
            int indexn2 = nString.indexOf(" ", indexn1);
            String regdate2 = nString.substring(indexn1, indexn2);
            int indexn3 = nString.indexOf(" ", indexn2);
            String s1s2 = nString.substring(indexn2,indexn3);
            int indexn4 = nString.indexOf(" ", indexn3);
            String s2s2 = nString.substring(indexn3,indexn4);
            int indexn5 = nString.indexOf(" ", indexn4);
            String s3s2 = nString.substring(indexn4,indexn5);
            int indexn6 = nString.indexOf(" ", indexn5);
            String s4s2 = nString.substring(indexn5,indexn6);
            int indexn7 = nString.indexOf(" ", indexn6);
            String s5s2 = nString.substring(indexn6,indexn7);

            tv1.setText(srno2);
            tv2.setText(regdate2);
            tv3.setText(s1s2);
            tv4.setText(s2s2);
            tv5.setText(s3s2);
            tv6.setText(s4s2);
            tv7.setText(s5s2);

            int indexN2 = nString.indexOf("\n",indexn);
            tempdbString = nString.substring(indexN2);
        }*/

    }


    //OnClick
    @Override
    public void onClick(View v) {

        switch (counter) {

            /*Target viewTarget2 = new ViewTarget(R.id.bViewTT, this);
            new ShowcaseView.Builder(this, true)
                    .setTarget(viewTarget2)
                    .setContentTitle("Timetable")
                    .setContentText("Edit and view your timetable.")
                    .singleShot(42)
                    .build();*/


            case 0:
                Target viewTarget2 = new ViewTarget(R.id.bViewTT, this);
                //new ShowcaseView.Builder(this, true);
                showcaseView.setTarget(viewTarget2);
                showcaseView.setContentTitle("Timetable");
                showcaseView.setContentText("Edit and view your timetable.");
                showcaseView.setStyle(R.style.CustomShowcaseTheme);
                //showcaseView.singleShot(42)


                break;

            case 1:
                Target viewTarget3 = new ViewTarget(R.id.radioGroup1, this);
                //new ShowcaseView.Builder(this, true)
                showcaseView.setTarget(viewTarget3);
                showcaseView.setContentTitle("Daily Ticker");
                showcaseView.setContentText("Daily set whether you were present/absent or the class wasn't engaged.");
                showcaseView.setStyle(R.style.CustomShowcaseTheme);
                //.singleShot(42)
                //.build();

                break;

            case 2:
                Target viewTarget4 = new ViewTarget(R.id.bView, this);
                //new ShowcaseView.Builder(this, true)
                showcaseView.setTarget(viewTarget4);
                showcaseView.setContentTitle("Update");
                showcaseView.setContentText("After choosing, Click Update to update the Register and Statistics");
                showcaseView.setStyle(R.style.CustomShowcaseTheme);
                //.singleShot(42)
                //.build();
                break;


            case 3:
                showcaseView.setTarget(Target.NONE);
                showcaseView.setContentTitle("Check updated databases");
                showcaseView.setContentText("Then click the Register|Statistics tabs.");
                showcaseView.setButtonText(getString(R.string.close));
                setAlpha(0.4f, bEditS, bViewTT, radioGroup1, bView);
                break;

            case 4:
                showcaseView.hide();
                setAlpha(1.0f, bEditS, bViewTT, radioGroup1, bView);
                showcaseView.clearAnimation();
                break;
        }
        counter++;


        switch (v.getId()) {

            case R.id.bViewTT:

                try {
                    Intent ourIntent = new Intent(MainActivity.this, Class.forName("com.myapps.aniruddha.myattendance.Timetable"));
                    startActivity(ourIntent);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error viewing the TimeTable!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;

            case R.id.bEditS:

                try {
                    Intent ourIntent = new Intent(MainActivity.this, Class.forName("com.myapps.aniruddha.myattendance.Edit"));
                    startActivity(ourIntent);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error editing the subjects!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;

            case R.id.bClear:

                try {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Clear Register");
                    builder.setMessage("Are you sure you want to clear the register?");
                    builder.setIcon(R.mipmap.notify);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHandler.clearDBString();
                                    tvStatus.setText(" ");
                                    Toast.makeText(builder.getContext(), "Register Cleared!", Toast.LENGTH_SHORT).show();

                                }
                            }
                    );
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    AlertDialog alert = builder.create();
                    alert.show();


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error while clearing!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;

            case R.id.bClearStats:
                try {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Clear Statistics");
                    builder.setMessage("Are you sure you want to clear all statistics?");
                    builder.setIcon(R.mipmap.notify);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbStatsHandler.clearDBString();
                                    dbStats2Handler.clearDBString();
                                    dbStats3Handler.clearDBString();
                                    dbStats4Handler.clearDBString();
                                    dbStats5Handler.clearDBString();
                                    Toast.makeText(builder.getContext(), "Statistics Cleared!", Toast.LENGTH_SHORT).show();
                                    printStats();

                                }
                            }
                    );
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    AlertDialog alert = builder.create();
                    alert.show();


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error while clearing!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.bDeleteRow:
                try {

                    final String sRow1 = etDelete.getText().toString();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete the row?");
                    builder.setIcon(R.mipmap.notify);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    long lRow1 = Long.parseLong(sRow1);

                                    dbHandler.deleteEntry(lRow1);
                                    etDelete.setText("");
                                    Toast.makeText(builder.getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                    printDatabase();

                                }
                            }
                    );
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    if (sRow1.isEmpty()) {
                        Toast.makeText(builder.getContext(), "Please Enter Row ID!", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Dang it!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.bView:
                try {
                    String thisMonth = "1";
                    String temptvD = tvDate.getText().toString();
                    int index = temptvD.indexOf(" ");
                    int index2 = temptvD.indexOf(",");
                    String viewMonth = temptvD.substring(0, index);
                    String viewDate = temptvD.substring(index, index2);
                    /*if (viewDate == "1" || viewDate == "2" || viewDate == "3" || viewDate == "4" || viewDate == "5" || viewDate == "6" || viewDate == "7" || viewDate == "8" || viewDate == "9" ){
                        viewDate = "0"+viewDate;
                    }*/

                    switch (viewDate) {
                        case " 1":
                            viewDate = "01";
                            break;
                        case " 2":
                            viewDate = "02";
                            break;
                        case " 3":
                            viewDate = "03";
                            break;
                        case " 4":
                            viewDate = "04";
                            break;
                        case " 5":
                            viewDate = "05";
                            break;
                        case " 6":
                            viewDate = "06";
                            break;
                        case " 7":
                            viewDate = "07";
                            break;
                        case " 8":
                            viewDate = "08";
                            break;
                        case " 9":
                            viewDate = "09";
                            break;
                    }

                    switch (viewMonth) {
                        case "January":
                            thisMonth = "01";
                            break;
                        case "February":
                            thisMonth = "02";
                            break;
                        case "March":
                            thisMonth = "03";
                            break;
                        case "April":
                            thisMonth = "04";
                            break;
                        case "May":
                            thisMonth = "05";
                            break;
                        case "June":
                            thisMonth = "06";
                            break;
                        case "July":
                            thisMonth = "07";
                            break;
                        case "August":
                            thisMonth = "08";
                            break;
                        case "September":
                            thisMonth = "09";
                            break;
                        case "October":
                            thisMonth = "10";
                            break;
                        case "November":
                            thisMonth = "11";
                            break;
                        case "December":
                            thisMonth = "12";
                            break;
                    }
                    tvD = viewDate + "/" + thisMonth;

                    updateDB udb = new updateDB(tvD, c1, c2, c3, c4, c5);
                    dbHandler.addProduct(udb);
                    //bUpdate.setText("Updated!");
                    printDatabase();

                    //                switchtoggle();


                    /*radioGroup1.check(R.id.rb3);
                    radioGroup2.check(R.id.r2b3);
                    radioGroup3.check(R.id.r3b3);
                    radioGroup4.check(R.id.r4b3);
                    radioGroup5.check(R.id.r5b3);*/

                    // setTTsubjects();

                    /*dbStatsHandler = new StatsDB(this, null, null, 1);
                    dbStats2Handler = new Stats2DB(this, null, null, 1);
                    dbStats3Handler = new Stats3DB(this, null, null, 1);
                    dbStats4Handler = new Stats4DB(this, null, null, 1);
                    dbStats5Handler = new Stats5DB(this, null, null, 1);*/

                    /*updateStats(s1te, s1ta);
                    updateStats2(s2te, s2ta);
                    updateStats3(s3te, s3ta);
                    updateStats4(s4te, s4ta);
                    updateStats5(s5te, s5ta);*/

                    printStats();

                    radioGroup1.check(R.id.rb3);
                    radioGroup2.check(R.id.r2b3);
                    radioGroup3.check(R.id.r3b3);
                    radioGroup4.check(R.id.r4b3);
                    radioGroup5.check(R.id.r5b3);

                    Toast.makeText(this, "Database Updated!", Toast.LENGTH_SHORT).show();

                    /*printColumn1Entries();
                    printColumn2Entries();
                    printColumn3Entries();
                    printColumn4Entries();
                    printColumn5Entries();*/


                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error Updating!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;

            case R.id.bUpdateStats:

                if (ets1te.getText().toString().isEmpty()) {
                    s1te = 0;
                } else {
                    s1te = Integer.parseInt(ets1te.getText().toString());
                }
                if (ets1ta.getText().toString().isEmpty()) {
                    s1ta = 0;
                } else {
                    s1ta = Integer.parseInt(ets1ta.getText().toString());
                }
                if (s1te == 0) {
                    isNEclicked = true;
                }
                updateStats(s1te, s1ta);
                if (ets2te.getText().toString().isEmpty()) {
                    s2te = 0;
                } else {
                    s2te = Integer.parseInt(ets2te.getText().toString());
                }
                if (ets2ta.getText().toString().isEmpty()) {
                    s2ta = 0;
                } else {
                    s2ta = Integer.parseInt(ets2ta.getText().toString());
                }
                if (s2te == 0) {
                    isNEclicked = true;
                }
                updateStats2(s2te, s2ta);

                if (ets3te.getText().toString().isEmpty()) {
                    s3te = 0;
                } else {
                    s3te = Integer.parseInt(ets3te.getText().toString());
                }
                if (ets3ta.getText().toString().isEmpty()) {
                    s3ta = 0;
                } else {
                    s3ta = Integer.parseInt(ets3ta.getText().toString());
                }
                if (s3te == 0) {
                    isNEclicked = true;
                }
                updateStats3(s3te, s3ta);

                if (ets4te.getText().toString().isEmpty()) {
                    s4te = 0;
                } else {
                    s4te = Integer.parseInt(ets4te.getText().toString());
                }
                if (ets4ta.getText().toString().isEmpty()) {
                    s4ta = 0;
                } else {
                    s4ta = Integer.parseInt(ets4ta.getText().toString());
                }
                if (s4te == 0) {
                    isNEclicked = true;
                }
                updateStats4(s4te, s4ta);

                if (ets5te.getText().toString().isEmpty()) {
                    s5te = 0;
                } else {
                    s5te = Integer.parseInt(ets5te.getText().toString());
                }
                if (ets5ta.getText().toString().isEmpty()) {
                    s5ta = 0;
                } else {
                    s5ta = Integer.parseInt(ets5ta.getText().toString());
                }
                if (s5te == 0) {
                    isNEclicked = true;
                }
                updateStats5(s5te, s5ta);


                printStats();

                Toast.makeText(this, "Statistics Updated!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /*Button ofm = (Button)findViewById(R.id.ofMenu);
        ofm.setOnClickListener(this);*/
        return true;
    }


    //  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        LinearLayout main_view = (LinearLayout) findViewById(R.id.main_view);

        switch (item.getItemId()) {

            case R.id.menu_yellow:
                try {
                    Intent ourIntent = new Intent(MainActivity.this, Class.forName("com.myapps.aniruddha.myattendance.Settings"));
                    startActivity(ourIntent);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;

            /*case R.id.menu_blue:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);*/
            case R.id.menu_red:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);

                try {
                    Intent ourIntent = new Intent(MainActivity.this, Class.forName("com.myapps.aniruddha.myattendance.Help"));
                    startActivity(ourIntent);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

                break;

                /*Dialog d = new Dialog(this);
                d.setTitle("Help");
                //d.getActionBar().setIcon(R.mipmap.ic_launcher);
                TextView tv = new TextView(this);
                tv.setPadding(7,7,7,7);
                tv.setScrollContainer(true);
                //tv.setTextColor(Color.argb(255,33,150,233));
                tv.setTextColor(Color.argb(255, 38, 50, 56));
                tv.setText("Click Edit Subjects button -> \n\n" +
                        "Edit Subjects: Edit the subject names as per your lectures. \n\n" +
                        "Set Minimum Attendance: Set the minimum attendance upto which you are allowed to bunk.\n" +
                        "Whenever the attendance in a subject gets lower than this minimum percentage, you will be notified accordingly. \n\n" +
                        "Click View Timetable button to set/view your Timetable anytime you wish. \n\n" +
                        "Daily Ticker: Choose the respected radio button if you were present or absent or the particular lecture was not engaged that day. \n\n" +
                        "After choosing, click UPDATE: Register and Statistics will be updated and saved! \n\n" +
                        "Change Date: Change the date if you wish to update the statistics/register for a given date. \n\n" +
                        "Edit Statistics: You can make changes in your statistics as per your wish. \n " +
                        "Then click Save Changes to save your edits.");
                d.setContentView(tv);
                d.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

        // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        *//*if (id == R.id.action_settings) {
            return true;
        }*//*

        //return super.onOptionsItemSelected(item);*/
        }

        return false;
    }
}

