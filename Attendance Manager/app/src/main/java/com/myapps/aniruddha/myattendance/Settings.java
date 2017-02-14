package com.myapps.aniruddha.myattendance;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends ActionBarActivity implements View.OnClickListener {

    Button bSaveMinP;
    //ToggleButton bTrue;
    Button bTrue;
    EditText etmin;
    String notifycs;
    //Boolean notifycs;
    TextView ivtext;
    TextView ivtext2;
    String bTrueText;
    float minp = 75;
    Float minPer;

    TextView switchStatus;
    private Switch mySwitch;

    String s1n, s2n, s3n, s4n, s5n, et1n, et2n, et3n, et4n, et5n, et1te, et2te, et3te, et4te, et5te;

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

    public NotificationManager nm5;
    public NotificationManager nm4;
    public NotificationManager nm3;
    public NotificationManager nm2;
    public NotificationManager nm;
    public NotificationManager nm0;


    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.settings);
            bSaveMinP = (Button) findViewById(R.id.bSaveMinP);
            bSaveMinP.setOnClickListener(this);
            etmin = (EditText) findViewById(R.id.etmin);

            switchStatus = (TextView) findViewById(R.id.switchStatus);
            mySwitch = (Switch) findViewById(R.id.mySwitch);

            //set the switch to ON
            mySwitch.setChecked(true);
            //attach a listener to check for changes in state
            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        switchStatus.setText("ON");
                    } else {
                        switchStatus.setText("OFF");
                        etmin.setText("");
                    }

                }


            });

            //check the current state before we display the screen
            if (mySwitch.isChecked()) {
                switchStatus.setText("ON");
            } else {
                switchStatus.setText("OFF");
            }


            SharedPreferences sharedstatus = getSharedPreferences("statusS", Context.MODE_PRIVATE);
            String status2 = sharedstatus.getString("statusS", "ON");
            if (status2.equals("ON")) {
                mySwitch.setChecked(true);
                SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);

                Float minPer = sharedPer.getFloat("minp", 75);

                etmin.setText(String.valueOf(minPer));
            }


            else
            mySwitch.setChecked(false);
            switchStatus.setText(status2);


        } catch (Exception e) {
            Dialog d = new Dialog(this);
            d.setTitle("Error E!");
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            d.setContentView(tv);
            d.show();
        }
    }
/*
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final TextView switchStatus = (TextView) findViewById(R.id.switchStatus);
        String status = switchStatus.getText().toString();
        outState.putString("savedText", status);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        final TextView switchStatus = (TextView) findViewById(R.id.switchStatus);
        String status = savedState.getString("savedText");

        switchStatus.setText(status);
    }*/


    //bTrueText = bTrue.getText().toString();

            /*ivtext = (TextView)findViewById(R.id.ivtext);
            ivtext2 = (TextView)findViewById(R.id.ivtext2);
            //bTrue= (ToggleButton) findViewById(R.id.bTrue);
            //bTrue = (Button) findViewById(R.id.bTrue);*/

    //SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);
    //minPer = sharedPer.getFloat("minp", 75);
    //etmin.setText(String.valueOf(minPer));


            /*SharedPreferences notifs = getSharedPreferences("notificsTF", Context.MODE_PRIVATE);
            Boolean tof = notifs.getBoolean("Tof", true);
            bTrue.setChecked(tof);
            notifycs = bTrue.getText().toString();*/



            /*SharedPreferences sharedPref = getSharedPreferences("subjectnames", Context.MODE_PRIVATE);

            s1n = sharedPref.getString("s1Name", "Subject1");
            s2n = sharedPref.getString("s2Name", "Subject2");
            s3n = sharedPref.getString("s3Name", "Subject3");
            s4n = sharedPref.getString("s4Name", "Subject4");
            s5n = sharedPref.getString("s5Name", "Subject5");

            SharedPreferences sharedets = getSharedPreferences("edittext", Context.MODE_PRIVATE);

            et1n = sharedets.getString("ets1", "0.0");
            et2n = sharedets.getString("ets2", "0.0");
            et3n = sharedets.getString("ets3", "0.0");
            et4n = sharedets.getString("ets4", "0.0");
            et5n = sharedets.getString("ets5", "0.0");

            et1te = sharedets.getString("etste1", "0");
            et2te = sharedets.getString("etste2", "0");
            et3te = sharedets.getString("etste3", "0");
            et4te = sharedets.getString("etste4", "0");
            et5te = sharedets.getString("etste5", "0");


            SharedPreferences notifs = getSharedPreferences("notificsTF", Context.MODE_PRIVATE);
            *//*SharedPreferences.Editor editor = notifs.edit();
            editor.putString("Tof",notifycs );
            editor.apply();*//*

            String tof = notifs.getString("Tof", "uhhh");
            bTrue.setText(tof);

            if(tof=="True"){

                notification = new NotificationCompat.Builder(this);
                notification.setAutoCancel(true);
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

                Double p1 = Double.parseDouble(et1n);
                Double p2 = Double.parseDouble(et2n);
                Double p3 = Double.parseDouble(et3n);
                Double p4 = Double.parseDouble(et4n);
                Double p5 = Double.parseDouble(et5n);

                Double minimumPercentage = minPer.doubleValue();

                if ((p5 < minimumPercentage) && (!(et5te.equals("0")))) {
                    //Build the notification
                    notification5.setSmallIcon(R.mipmap.notify);
                    notification5.setTicker("Have a look at your attendance!");
                    notification5.setWhen(System.currentTimeMillis());
                    notification5.setContentTitle("Low Attendance!");
                    notification5.setContentText("Your attendance in " + s5n + " is " + String.valueOf(p5) + "%");
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
                    //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
                    notification5.setContentIntent(pendingIntent);
                    notification5.setDeleteIntent(pendingIntent);


                    //Builds notifications and issues it
                    nm5 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm5.notify(uniqueID5, notification5.build());


                }
                if ((p4 < minimumPercentage) && (!(et4te.equals("0")))) {
                    //Build the notification
                    notification4.setSmallIcon(R.mipmap.notify);
                    notification4.setTicker("Have a look at your attendance!");
                    notification4.setWhen(System.currentTimeMillis());
                    notification4.setContentTitle("Low Attendance!");
                    notification4.setContentText("Your attendance in " + s4n + " is " + String.valueOf(p4) + "%");
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification4.setContentIntent(pendingIntent);

                    //Builds notifications and issues it
                    nm4 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm4.notify(uniqueID4, notification4.build());
                }
                if ((p3 < minimumPercentage) && (!(et3te.equals("0")))) {
                    //Build the notification
                    notification3.setSmallIcon(R.mipmap.notify);
                    notification3.setTicker("Have a look at your attendance!");
                    notification3.setWhen(System.currentTimeMillis());
                    notification3.setContentTitle("Low Attendance!");
                    notification3.setContentText("Your attendance in " + s3n + " is " + String.valueOf(p3) + "%");
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification3.setContentIntent(pendingIntent);

                    //Builds notifications and issues it
                    nm3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm3.notify(uniqueID3, notification3.build());
                }
                if ((p2 < minimumPercentage) && (!(et2te.equals("0")))) {
                    //Build the notification
                    notification2.setSmallIcon(R.mipmap.notify);
                    notification2.setTicker("Have a look at your attendance!");
                    notification2.setWhen(System.currentTimeMillis());
                    notification2.setContentTitle("Low Attendance!");
                    notification2.setContentText("Your attendance in " + s2n + " is " + String.valueOf(p2) + "%");
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification2.setContentIntent(pendingIntent);

                    //Builds notifications and issues it
                    nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm2.notify(uniqueID2, notification2.build());
                }
                if ((p1 < minimumPercentage) && (!(et1te.equals("0")))) {
                    //Build the notification
                    notification.setSmallIcon(R.mipmap.notify);
                    notification.setTicker("Have a look at your attendance!");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Low Attendance!");
                    notification.setContentText("Your attendance in " +s1n + " is " + String.valueOf(p1) + "%");

                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    //Builds notifications and issues it
                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());
                }
            }
            else{
                ivtext2.setText("Not Done");
                bSaveMinP.setText("Not Done");
            }

            */


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*
            case R.id.bTrue:

                try {
                    /*if (bTrue.isChecked()) {
                        bTrue.setChecked(false);
                        bTrue.setText("False");
                        notifycs = false;
                    } else {
                        bTrue.setChecked(true);
                        bTrue.setText("True");
                        notifycs = true;
                    }

                    if(bTrueText.equals("True")) {
                        bTrue.setText("False");
                        ivtext.setText("False");
                    }
                    else if (bTrueText.equals("False")) {
                        bTrue.setText("True");
                        ivtext.setText("True");
                    }

                    SharedPreferences notifs = getSharedPreferences("notificsTF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = notifs.edit();
                    editor.putString("Tof", ivtext.getText().toString());
                    //editor.putBoolean("Tof", notifycs);
                    editor.apply();

                    //Boolean tof = notifs.getBoolean("Tof", true);
                    //bTrue.setChecked(tof);
                    String tof = notifs.getString("Tof", "hmmm");
                    ivtext2.setText(tof);

                    Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;
*/
            case R.id.bSaveMinP:
                try {

                    String status = switchStatus.getText().toString();
                    SharedPreferences sharedstatus = getSharedPreferences("statusS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorS = sharedstatus.edit();

                    editorS.putString("statusS", status);
                    editorS.apply();

                    String status2 = sharedstatus.getString("statusS", "ON");
                    switchStatus.setText(status2);


                    if (status.equals("ON")) {

                        if (!etmin.getText().toString().isEmpty()) {
                            Intent i = new Intent(this, MainActivity.class);
                            Float minp = Float.parseFloat(etmin.getText().toString());
                            //i.putExtra("minp", minp);
                            SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = sharedPer.edit();

                            editorP.putFloat("minp", minp);
                            editorP.apply();

                            Float minPer = sharedPer.getFloat("minp", 75);

                            etmin.setText(String.valueOf(minPer));

                            Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            Toast.makeText(this, "Please enter a value!", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        if (etmin.getText().toString().isEmpty()) {
                            Intent i = new Intent(this, MainActivity.class);
                            SharedPreferences sharedPer = getSharedPreferences("minper", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = sharedPer.edit();
                            editorP.putFloat("minp", 0);
                            editorP.apply();

                            Float minPer = sharedPer.getFloat("minp", 75);
                            //etmin.setText(String.valueOf(minPer));

                            Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            Toast.makeText(this, "Please first set the Notifications ON", Toast.LENGTH_LONG).show();
                            etmin.setText("");
                        }

                    }

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }

        }
    }

}
