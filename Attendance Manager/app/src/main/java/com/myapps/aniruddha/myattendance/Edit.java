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

public class Edit extends ActionBarActivity implements View.OnClickListener {


    TextView tvs1, tvs2, tvs3, tvs4, tvs5, tvDate, tvDay;
    EditText edittvs1, edittvs2, edittvs3, edittvs4, edittvs5;
    Button bSaveS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        bSaveS = (Button) findViewById(R.id.bSaveS);
        bSaveS.setOnClickListener(this);


        edittvs1 = (EditText) findViewById(R.id.edittvs1);
        edittvs2 = (EditText) findViewById(R.id.edittvs2);
        edittvs3 = (EditText) findViewById(R.id.edittvs3);
        edittvs4 = (EditText) findViewById(R.id.edittvs4);
        edittvs5 = (EditText) findViewById(R.id.edittvs5);



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

            case R.id.bSaveS:

                try {

                    tvs1 = (TextView) findViewById(R.id.tvs1);
                    tvs2 = (TextView) findViewById(R.id.tvs2);
                    tvs3 = (TextView) findViewById(R.id.tvs3);
                    tvs4 = (TextView) findViewById(R.id.tvs4);
                    tvs5 = (TextView) findViewById(R.id.tvs5);

                    Intent i = new Intent(this, MainActivity.class);

                    String s1name = edittvs1.getText().toString();
                    //i.putExtra("s1Name", s1name);
                    String s2name = edittvs2.getText().toString();
                    //i.putExtra("s2Name", s2name);
                    String s3name = edittvs3.getText().toString();
                    //i.putExtra("s3Name", s3name);
                    String s4name = edittvs4.getText().toString();
                    //i.putExtra("s4Name", s4name);
                    String s5name = edittvs5.getText().toString();
                    //i.putExtra("s5Name", s5name);

                    SharedPreferences sharedPref = getSharedPreferences("subjectnames", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("s1Name", s1name);
                    editor.putString("s2Name", s2name);
                    editor.putString("s3Name", s3name);
                    editor.putString("s4Name", s4name);
                    editor.putString("s5Name", s5name);

                    editor.apply();

                    String s1n = sharedPref.getString("s1Name", "Subject1");
                    String s2n = sharedPref.getString("s2Name", "Subject2");
                    String s3n = sharedPref.getString("s3Name", "Subject3");
                    String s4n = sharedPref.getString("s4Name", "Subject4");
                    String s5n = sharedPref.getString("s5Name", "Subject5");

                    tvs1.setText(s1n);
                    tvs2.setText(s2n);
                    tvs3.setText(s3n);
                    tvs4.setText(s4n);
                    tvs5.setText(s5n);

                    Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
                    startActivity(i);

                } catch (Exception e) {
                    Dialog d = new Dialog(this);
                    d.setTitle("Error!");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    d.setContentView(tv);
                    d.show();
                }
                break;
        }
    }


}
