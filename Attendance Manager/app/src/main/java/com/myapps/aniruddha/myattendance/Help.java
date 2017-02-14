package com.myapps.aniruddha.myattendance;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class Help extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {

        try{

        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

    } catch (Exception e) {
        Dialog d = new Dialog(this);
        d.setTitle("Error E!");
        TextView tv = new TextView(this);
        tv.setText(e.toString());
        d.setContentView(tv);
        d.show();
    }
    }
}
