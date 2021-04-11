package com.example.icctpassapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    String Email, Password;

    public static final String DBNAME = "Database.db";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_REPASSWORD = "Repassword";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLASS_NAME = "className";
    public static final String COLUMN_SUBJECT_CODE = "subjectCode";
    public static final String COLUMN_SECTION = "section";

    public static final String COLUMN_ID2 = "id";
    public static final String COLUMN_EVENT_NAME = "eventName";
    public static final String COLUMN_EVENT_LOCATION = "eventLocation";
    public static final String COLUMN_TIME = "time";

    public static final String COLUMN_ID_CLASS = "id";
    public static final String COLUMN_TYPE_CLASS = "Type";
    public static final String COLUMN_CONTENT_CLASS = "Content";

    public static final String COLUMN_ID_EVENT = "id";
    public static final String COLUMN_TYPE_EVENT = "Type";
    public static final String COLUMN_CONTENT_EVENT  = "Content";

    public DataBase(Context context) {
        super(context, "Database.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users (Email TEXT, Password TEXT, Repassword TEXT)");
        MyDB.execSQL("create Table classrooms (id INTEGER PRIMARY KEY AUTOINCREMENT, className TEXT, subjectCode TEXT, section TEXT)");
        MyDB.execSQL("create Table events (id INTEGER PRIMARY KEY AUTOINCREMENT, eventName TEXT, eventLocation TEXT, time TEXT)");
        MyDB.execSQL("create Table class (id INTEGER PRIMARY KEY AUTOINCREMENT, Type TEXT, Content TEXT)");
        MyDB.execSQL("create Table event (id INTEGER PRIMARY KEY AUTOINCREMENT, Type TEXT, Content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if Exists users");
        MyDB.execSQL("drop Table if Exists classrooms");
        MyDB.execSQL("drop Table if Exists events");
        MyDB.execSQL("drop Table if Exists class");
        MyDB.execSQL("drop Table if Exists event");
    }

    public boolean insertData(String Email, String Password, String Repassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", Email);
        contentValues.put("Password", Password);
        contentValues.put("Repassword", Repassword);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertClass(String className, String subjectCode, String section) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("subjectCode", subjectCode);
        contentValues.put("section", section);
        long result = MyDB.insert("classrooms", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertEvent(String eventName, String eventLocation, String time) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventName", eventName);
        contentValues.put("eventLocation", eventLocation);
        contentValues.put("time", time);
        long result = MyDB.insert("events", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    Cursor viewData(){
        String query = "SELECT * FROM classrooms";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor viewEvent(){
        String query = "SELECT * FROM events";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor viewScanStudentData(){
        String query = "SELECT * FROM class";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor viewAttendeesData(){
        String query = "SELECT * FROM event";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public boolean insertScanStudents(String Type, String Content) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Type", Type);
        contentValues.put("Content", Content);
        long result = MyDB.insert("class", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertScanAttendees(String Type, String Content) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Type", Type);
        contentValues.put("Content", Content);
        long result = MyDB.insert("event", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String Password, String Repassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Password", Password);
        contentValues.put("Repassword", Repassword);
        Cursor cursor = MyDB.rawQuery("Select * from users where Email=?", new String[]{Email});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("users", contentValues, ("Email = ?"), new String[]{Email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean deleteData(String Email, String Password, String Repassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Email=? +  Password=? + Repassword=?", new String[]{Email, Password, Repassword});
        if (cursor.getCount() > 0) {
            long result = MyDB.delete("users", ("Email=?" + "Password=?" + "Repassword=?"), new String[]{Email, Password, Repassword});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean checkemail(String Email) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Email=?", new String[]{Email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkemailpassword(String Email, String Password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Email=? and Password=?", new String[]{Email, Password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}