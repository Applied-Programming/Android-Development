package com.myapps.aniruddha.myattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "the_date";
    public static final String KEY_SUBJECT1 = "subject_1";
    public static final String KEY_SUBJECT2 = "subject_2";
    public static final String KEY_SUBJECT3 = "subject_3";
    public static final String KEY_SUBJECT4 = "subject_4";
    public static final String KEY_SUBJECT5 = "subject_5";

    private static final String DATABASE_NAME = "Attendancedb";
    private static final String DATABASE_TABLE = "register";
    private static final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        //OnCreate
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_DATE + " TEXT NOT NULL, " +
                    KEY_SUBJECT1 + " TEXT NOT NULL, " +
                    KEY_SUBJECT2 + " TEXT NOT NULL, " +
                    KEY_SUBJECT3 + " TEXT NOT NULL, " +
                    KEY_SUBJECT4 + " TEXT NOT NULL, " +
                    KEY_SUBJECT5 + " TEXT NOT NULL" +
                    ");";
            db.execSQL(query); //execute sql query
        }

        //On Update
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE); //delete table
            onCreate(db); //recreate another db
        }
    }

    //Constructor
    public Database(Context c) {
        ourContext = c;
    }

    //open()
    public Database open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    //close()
    public void close() {
        ourHelper.close();
    }

    //update dbview after button clicks
    public long createEntry(String date, String s1, String s2, String s3, String s4, String s5) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, date);
        cv.put(KEY_SUBJECT1, s1);
        cv.put(KEY_SUBJECT2, s2);
        cv.put(KEY_SUBJECT3, s3);
        cv.put(KEY_SUBJECT4, s4);
        cv.put(KEY_SUBJECT5, s5);
        return ourDatabase.insert(DATABASE_TABLE, null, cv); //insert into db

    }

    //Get/Read the data
    public String getData() throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};

        //Cursor is used to read the table from db
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iDate = c.getColumnIndex(KEY_DATE);
        int iS1 = c.getColumnIndex(KEY_SUBJECT1);
        int iS2 = c.getColumnIndex(KEY_SUBJECT2);
        int iS3 = c.getColumnIndex(KEY_SUBJECT3);
        int iS4 = c.getColumnIndex(KEY_SUBJECT4);
        int iS5 = c.getColumnIndex(KEY_SUBJECT5);


        //Read the data
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = result + c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iS1) + " " + c.getString(iS2) + " " + c.getString(iS3) + " " + c.getString(iS4) + " " + c.getString(iS5) + "\n";
        }
        return result;
    }

    public String getDate(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String date = c.getString(1);
            return date;
        }
        return null;
    }

    //later
    public String getS1(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String s1 = c.getString(2);
            return s1;
        }
        return null;
    }

    public String getS2(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String s2 = c.getString(3);
            return s2;
        }
        return null;
    }

    public String getS3(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String s3 = c.getString(4);
            return s3;
        }
        return null;
    }

    public String getS4(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String s4 = c.getString(5);
            return s4;
        }
        return null;
    }

    public String getS5(long l) throws SQLException {
        String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_SUBJECT1, KEY_SUBJECT2, KEY_SUBJECT3, KEY_SUBJECT4, KEY_SUBJECT5};
        //parameter selection is set to ( KEY_ROWID + "=" + l )
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String s5 = c.getString(6);
            return s5;
        }
        return null;
    }


    public void updateEntry(long lRow, String mDate, String mS1, String mS2, String mS3, String mS4, String mS5) throws SQLException {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_DATE, mDate);
        cvUpdate.put(KEY_SUBJECT1, mS1);
        cvUpdate.put(KEY_SUBJECT2, mS2);
        cvUpdate.put(KEY_SUBJECT3, mS3);
        cvUpdate.put(KEY_SUBJECT4, mS4);
        cvUpdate.put(KEY_SUBJECT5, mS5);
        ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + lRow, null); //insert into db
    }

    public void deleteEntry(long lRow1) throws SQLException {
        ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);
    }


}