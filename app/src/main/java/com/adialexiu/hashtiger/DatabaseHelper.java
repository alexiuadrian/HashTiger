package com.adialexiu.hashtiger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "thash.db";
    private static final String TABLE_NAME = "accounts";
    private static final int DATABASE_VERSION = 1;
    //private static final UUID uuid = UUID.randomUUID();
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            " (" + KEY_ID + " text, " + KEY_USERNAME + " text, " + KEY_PASSWORD + " text)";
    private static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Toast.makeText(context, "constructor called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch (SQLException e) {
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean insertUser(String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USERNAME, userName);
        contentValues.put(KEY_PASSWORD, password);
        // Daca nu e in baza de date, baga-l
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public boolean isUserInDb(String usernameText) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = getAllUsers();
        if (res.getCount() != 0) {
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {
                if(usernameText.equals(res.getString(res.getColumnIndex(KEY_USERNAME))))
                    return true;
            }
        }
        return false;
    }
}
