package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Stats2DB extends SQLiteOpenHelper {

 private static final int DATABASE_VERSION = 1;
 private static final String DATABASE_NAME = "stats2DB.db";
 private static final String TABLE_STATS2 = "stats2";
 private static final String COLUMN_ID = "_ids";
 private static final String COLUMN_STATS1 = "te2stats";
 private static final String COLUMN_STATS2 = "ta2stats";
 private static final String COLUMN_STATS3 = "p2stats";


 //pass db info to superclass
 public Stats2DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
  super(context, DATABASE_NAME, factory, DATABASE_VERSION);
 }

 @Override
 public void onCreate(SQLiteDatabase db) {
  String query = "CREATE TABLE " + TABLE_STATS2 + "(" +
          COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
          COLUMN_STATS1 + " TEXT, " +
          COLUMN_STATS2 + " TEXT, " +
          COLUMN_STATS3 + " TEXT " +
          ");";
  db.execSQL(query); //execute sql query
 }

 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  db.execSQL("DROP TABLE IF EXISTS" + TABLE_STATS2); //delete table
  onCreate(db); //recreate another db
 }

 public void addProduct2(UpdateStats2 us2){
  ContentValues values = new ContentValues(); //insert a new row/values
  values.put(COLUMN_STATS1, us2.get_te());
  values.put(COLUMN_STATS2, us2.get_ta());
  values.put(COLUMN_STATS3, us2.get_p());
  SQLiteDatabase db = getWritableDatabase();
  db.insert(TABLE_STATS2, null, values);
  db.close();
 }

    /*//delete a product from the db
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }*/

 //print out the db as a string
 public String databasete2Stats(){
  String teString = "0";
  SQLiteDatabase db = getWritableDatabase();
  String query  = " SELECT * FROM " + TABLE_STATS2 + " WHERE 1 ";
  //Cursor points to location in your results
  Cursor c  = db.rawQuery(query, null);
  //Move to the first row in ur results
  c.moveToFirst();
  //position after the last row means the end of the table
  while (!c.isAfterLast()){
   //if(c.getString(c.getColumnIndex("productname"))!=null){
   teString = c.getString(c.getColumnIndex("te2stats"));
   //}
   c.moveToNext();
  }
  db.close();//close
  return teString;
 }
 public String databaseta2Stats(){
  String taString = "0";
  SQLiteDatabase db = getWritableDatabase();
  String query  = " SELECT * FROM " + TABLE_STATS2 + " WHERE 1 ";
  //Cursor points to location in your results
  Cursor c  = db.rawQuery(query, null);
  //Move to the first row in ur results
  c.moveToFirst();
  //position after the last row means the end of the table
  while (!c.isAfterLast()){
   //if(c.getString(c.getColumnIndex("productname"))!=null){
   taString = c.getString(c.getColumnIndex("ta2stats"));
   //}
   c.moveToNext();
  }
  db.close();//close
  return taString;
 }
 public String databasep2Stats(){
  String pString = "0.0";
  SQLiteDatabase db = getWritableDatabase();
  String query  = " SELECT * FROM " + TABLE_STATS2 + " WHERE 1 ";
  //Cursor points to location in your results
  Cursor c  = db.rawQuery(query, null);
  //Move to the first row in ur results
  c.moveToFirst();
  //position after the last row means the end of the table
  while (!c.isAfterLast()){
   //if(c.getString(c.getColumnIndex("productname"))!=null){
   pString = c.getString(c.getColumnIndex("p2stats"));
   //}
   c.moveToNext();
  }
  db.close();//close
  return pString;
 }

    /*public void addProduct2(UpdateStats2 us2){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN2_STATS1, us2.get_te());
        values.put(COLUMN2_STATS2, us2.get_ta());
        values.put(COLUMN2_STATS3, us2.get_p());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STATS, null, values);
        db.close();
    }

    //Second row of stats

    public String databasete2Stats(){
        String teString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS + " WHERE 1 ";
        Cursor c2  = db.rawQuery(query, null);
        c2.moveToFirst();
        while (!c2.isAfterLast()){
            teString = c2.getString(c2.getColumnIndex("te2stats"));
            c2.moveToNext();
        }
        db.close();
        return teString;
    }
    public String databaseta2Stats(){
        String taString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS + " WHERE 1 ";
        Cursor c2  = db.rawQuery(query, null);
        c2.moveToFirst();
        while (!c2.isAfterLast()){
            taString = c2.getString(c2.getColumnIndex("ta2stats"));
            c2.moveToNext();
        }
        db.close();
        return taString;
    }
    public String databasep2Stats(){
        String pString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS + " WHERE 1 ";
        Cursor c2  = db.rawQuery(query, null);
        c2.moveToFirst();
        while (!c2.isAfterLast()){
            pString = c2.getString(c2.getColumnIndex("p2stats"));
            c2.moveToNext();
        }
        db.close();
        return pString;
    }*/
    /*public void clearDBString(){
        *//*String clearString = databaseToString();
        clearString = " ";*//*
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE 1" + ";");
    }*/
    public void clearDBString(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS2 + " WHERE 1" + ";");
    }
}


