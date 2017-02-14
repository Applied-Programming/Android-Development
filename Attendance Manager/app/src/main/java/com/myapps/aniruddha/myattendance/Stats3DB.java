package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Stats3DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "stats3DB.db";
    private static final String TABLE_STATS3 = "stats3";
    private static final String COLUMN_ID = "_ids";
    private static final String COLUMN_STATS1 = "te3stats";
    private static final String COLUMN_STATS2 = "ta3stats";
    private static final String COLUMN_STATS3 = "p3stats";


    //pass db info to superclass
    public Stats3DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_STATS3 + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATS1 + " TEXT, " +
                COLUMN_STATS2 + " TEXT, " +
                COLUMN_STATS3 + " TEXT " +
                ");";
        db.execSQL(query); //execute sql query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_STATS3); //delete table
        onCreate(db); //recreate another db
    }

    public void addProduct3(UpdateStats3 us3){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN_STATS1, us3.get_te());
        values.put(COLUMN_STATS2, us3.get_ta());
        values.put(COLUMN_STATS3, us3.get_p());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STATS3, null, values);
        db.close();
    }

    /*//delete a product from the db
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }*/

    //print out the db as a string
    public String databasete3Stats(){
        String teString = "0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS3 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            teString = c.getString(c.getColumnIndex("te3stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return teString;
    }
    public String databaseta3Stats(){
        String taString = "0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS3 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            taString = c.getString(c.getColumnIndex("ta3stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return taString;
    }
    public String databasep3Stats(){
        String pString = "0.0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS3 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            pString = c.getString(c.getColumnIndex("p3stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return pString;
    }

    public void clearDBString(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS3 + " WHERE 1" + ";");
    }
}


