package com.example.icctpassapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    String email, password;

    public static final String DBNAME = "Data.db";

    public DataBase(Context context) {
        super(context, "Data.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users (LastName TEXT, FirstName TEXT, MiddleName TEXT,ContactNumber TEXT, Email TEXT ,Password TEXT, Repassword TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if Exists users");
    }

    public boolean insertData(String LastName, String FirstName, String MiddleName, String ContactNumber, String Email, String Password, String Repassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LastName", LastName);
        contentValues.put("FirstName", FirstName);
        contentValues.put("MiddleName", MiddleName);
        contentValues.put("ContactNumber", ContactNumber);
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