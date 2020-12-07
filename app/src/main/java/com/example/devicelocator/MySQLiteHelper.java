package com.example.devicelocator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.CamcorderProfile;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    //Initialize variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "login.db";
    private static final String TABLE_NAME = "users";

    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table with required columns
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_USER_ID + " Integer PRIMARY KEY," +
                COL_USER_NAME +  " TEXT, " +
                COL_EMAIL +  " TEXT," +
                COL_PASSWORD +  " TEXT" + ")";
        Log.d("DBText","createTable: "+createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table IF EXISTS " + TABLE_NAME + ";");
        this.onCreate(db);
    }

    //input user information upon registration
    public boolean addUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.Name);
        values.put(COL_EMAIL,user.Email);
        values.put(COL_PASSWORD,user.Password);

        long result= db.insert(TABLE_NAME,null,values);
        db.close();
        if (result ==0) return false;
        else
            return true;
    }

    //Authenticate user information for login
    public User Authentication(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[] {COL_USER_ID,COL_USER_NAME,COL_EMAIL,COL_PASSWORD},
                COL_EMAIL + "=?",
                new String[] {user.Email},
                null,null,null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount()>0){
            User user1 = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
            if(user.Password.equalsIgnoreCase((user1.Password))){
                return user1;
            }
        }
        return null;
    }
    //Verify user email
    public boolean emailVerification(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,// Selecting Table
                new String[]{COL_USER_ID, COL_USER_NAME,COL_EMAIL,COL_PASSWORD},//Selecting columns want to query
                COL_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
}
