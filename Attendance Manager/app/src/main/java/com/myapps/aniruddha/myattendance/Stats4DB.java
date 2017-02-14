package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Stats4DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "stats4DB.db";
    private static final String TABLE_STATS4 = "stats4";
    private static final String COLUMN_ID = "_ids";
    private static final String COLUMN_STATS1 = "te4stats";
    private static final String COLUMN_STATS2 = "ta4stats";
    private static final String COLUMN_STATS3 = "p4stats";


    //pass db info to superclass
    public Stats4DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_STATS4 + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATS1 + " TEXT, " +
                COLUMN_STATS2 + " TEXT, " +
                COLUMN_STATS3 + " TEXT " +
                ");";
        db.execSQL(query); //execute sql query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_STATS4); //delete table
        onCreate(db); //recreate another db
    }

    public void addProduct4(UpdateStats4 us4){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN_STATS1, us4.get_te());
        values.put(COLUMN_STATS2, us4.get_ta());
        values.put(COLUMN_STATS3, us4.get_p());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STATS4, null, values);
        db.close();
    }

    /*//delete a product from the db
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }*/

    //print out the db as a string
    public String databasete4Stats(){
        String teString = "0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS4 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            teString = c.getString(c.getColumnIndex("te4stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return teString;
    }
    public String databaseta4Stats(){
        String taString = "0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS4 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            taString = c.getString(c.getColumnIndex("ta4stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return taString;
    }
    public String databasep4Stats(){
        String pString = "0.0";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_STATS4 + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        //Move to the first row in ur results
        c.moveToFirst();
        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            pString = c.getString(c.getColumnIndex("p4stats"));
            //}
            c.moveToNext();
        }
        db.close();//close
        return pString;
    }

    public void clearDBString(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATS4 + " WHERE 1" + ";");
    }
}


