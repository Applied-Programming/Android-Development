package com.myapps.vidyalay;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Education extends AppCompatActivity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Adding menu icon to Toolbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle("  Education  |  शिक्षण");

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                switch (item.getTitle()) {

                    case "Image#0":
                        Intent intent = new Intent(Education.this, Class1.class);
                        startActivity(intent);
                        break;

                    case "Image#1":
                        Intent intent2 = new Intent(Education.this, Class2.class);
                        startActivity(intent2);
                        break;
                    case "Image#2":
                        Intent intent3 = new Intent(Education.this, Class3.class);
                        startActivity(intent3);
                        break;
                    case "Image#3":
                        Intent intent4 = new Intent(Education.this, Class4.class);
                        startActivity(intent4);
                        break;
                    case "Image#4":
                        Intent intent5 = new Intent(Education.this, Class5.class);
                        startActivity(intent5);
                        break;
                    case "Image#5":
                        Intent intent6 = new Intent(Education.this, Class6.class);
                        startActivity(intent6);
                        break;
                    case "Image#6":
                        Intent intent7 = new Intent(Education.this, Class7.class);
                        startActivity(intent7);
                        break;
                    case "Image#7":
                        Intent intent8 = new Intent(Education.this, Class8.class);
                        startActivity(intent8);
                        break;
                    case "Image#8":
                        Toast.makeText(getApplicationContext(), "Tutorials for Class 9th to 12th: Coming soon", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        });
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }
}