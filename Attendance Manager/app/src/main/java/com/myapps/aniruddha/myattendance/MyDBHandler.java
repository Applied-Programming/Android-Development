package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper{

    public int iRow;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productsDB.db";
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_PRODUCTNAME1 = "productname1";
    private static final String COLUMN_PRODUCTNAME2 = "productname2";
    private static final String COLUMN_PRODUCTNAME3 = "productname3";
    private static final String COLUMN_PRODUCTNAME4 = "productname4";
    private static final String COLUMN_PRODUCTNAME5 = "productname5";

    //pass db info to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    try {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_PRODUCTNAME1 + " TEXT, " +
                COLUMN_PRODUCTNAME2 + " TEXT, " +
                COLUMN_PRODUCTNAME3 + " TEXT, " +
                COLUMN_PRODUCTNAME4 + " TEXT, " +
                COLUMN_PRODUCTNAME5 + " TEXT " +
                ");";
        db.execSQL(query); //execute sql query
    }catch(Exception e){}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PRODUCTS); //delete table
        onCreate(db); //recreate another db
    }

    //Add a new row to the db
    /*public void addProduct(String date,Products product1){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN_PRODUCTNAME1, date);
        values.put(COLUMN_PRODUCTNAME, product1.get_productname());
        //values.put(COLUMN_PRODUCTNAME2, product2.get_productname());
        values.put(COLUMN_PRODUCTNAME2, "...");
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }*/
    public void addProduct(updateDB udb){
        ContentValues values = new ContentValues(); //insert a new row/values
        values.put(COLUMN_PRODUCTNAME, udb.get_date());
        values.put(COLUMN_PRODUCTNAME1, udb.get_c1());
        //values.put(COLUMN_PRODUCTNAME2, product2.get_productname());
        values.put(COLUMN_PRODUCTNAME2, udb.get_c2());
        values.put(COLUMN_PRODUCTNAME3, udb.get_c3());
        values.put(COLUMN_PRODUCTNAME4, udb.get_c4());
        values.put(COLUMN_PRODUCTNAME5, udb.get_c5());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    //delete a product from the db
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }

    public void deleteEntry(long lRow1)throws SQLException {
        SQLiteDatabase db2 = getWritableDatabase();
        //db2.delete(TABLE_PRODUCTS, COLUMN_ID + "=" + lRow1, null);
        db2.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + "=\"" + lRow1 + "\";");


    }

    //print out the db as a string
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = " SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1 ";
        //Cursor points to location in your results
        Cursor c  = db.rawQuery(query, null);
        iRow = c.getColumnIndex(COLUMN_ID);
        //Move to the first row in ur results
        c.moveToFirst();

        //position after the last row means the end of the table
        while (!c.isAfterLast()){
            //if(c.getString(c.getColumnIndex("productname"))!=null){
            String Rowno = c.getString(c.getColumnIndex("_id"));
            /*if (Rowno == "1" || Rowno == "2" || Rowno == "3" || Rowno == "4" || Rowno == "5" || Rowno == "6" || Rowno == "7" || Rowno == "8" || Rowno == "9" ){
                Rowno = "0"+Rowno;
            }*/
            switch (Rowno) {
                case "1":
                    Rowno= "01";
                    break;
                case  "2":
                    Rowno = "02";
                    break;
                case  "3":
                    Rowno = "03";
                    break;
                case "4":
                    Rowno = "04";
                    break;
                case "5":
                    Rowno = "05";
                    break;
                case "6":
                    Rowno = "06";
                    break;
                case "7":
                    Rowno = "07";
                    break;
                case "8":
                    Rowno = "08";
                    break;
                case "9":
                    Rowno = "09";
                    break;
                default:Rowno = Rowno;
            }

                dbString += Rowno + "    " +c.getString(c.getColumnIndex("productname"))+ "    " + c.getString(c.getColumnIndex("productname1"))+ "    " + c.getString(c.getColumnIndex("productname2"))+ "    " + c.getString(c.getColumnIndex("productname3"))+ "    " + c.getString(c.getColumnIndex("productname4"))+ "    " + c.getString(c.getColumnIndex("productname5"));
                dbString += "\n";
            //}
            c.moveToNext();
        }
        db.close();//close
        return dbString;
    }

    public void clearDBString(){
        /*String clearString = databaseToString();
        clearString = " ";*/

/*
        final Context ourContext = ;
        MyDBHandler ourHelper;
        SQLiteDatabase ourDatabase;
        ourHelper = new MyDBHandler(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();

*/

        SQLiteDatabase db = getWritableDatabase();
        //db.delete(TABLE_PRODUCTS,COLUMN_ID  + "=" + iRow, null);
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + ";");
        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{TABLE_PRODUCTS});
        //iRow=1;
        /*String query  = " SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1 ";
        Cursor c  = db.rawQuery(query, null);
        c.moveToFirst();*/

        //db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE 1" + ";");
        db.close();//close
    }
}
