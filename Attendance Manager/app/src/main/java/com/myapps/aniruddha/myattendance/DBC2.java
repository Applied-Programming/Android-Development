package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBC2  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productsc2DB.db";
    private static final String TABLE_PRODUCTS = "productsC2";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productname";

    //pass db info to superclass
    public DBC2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT " +
                ");";
        db.execSQL(query); //execute sql query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PRODUCTS); //delete table
        onCreate(db); //recreate another db
    }

    //Add a new row to the db
    public void addEntries(Products product){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN_PRODUCTNAME, product.get_productname());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    //delete a product from the db
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }

    //print out the db as a string
    public String printColumn(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();

        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("productname"))!=null){
                dbString += c.getString(c.getColumnIndex("productname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();//close
        return dbString;
    }
}

