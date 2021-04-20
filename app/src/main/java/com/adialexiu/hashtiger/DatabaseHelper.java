package com.adialexiu.hashtiger;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "thash.db";
    private static final String TABLE_NAME = "accounts";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            " (" + KEY_ID + " text, " + KEY_USERNAME + " text, " + KEY_PASSWORD + " text)";
    private static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private static final String TABLE_NAME_1 = "credentials";
    private static final String SERVICE_NAME = "serviceName";
    private static final String FK_USER = "credential_owner";
    private static final String CREATE_TABLE_1 = "create table " + TABLE_NAME_1 +
            " (" + KEY_ID + " text, " + SERVICE_NAME + " text, " + KEY_USERNAME +
            " text, " + KEY_PASSWORD + " text, " + FK_USER + " text )";

    private static final String DROP_TABLE_1 = "drop table if exists " + TABLE_NAME_1;

    private Context context;

    List<Credential> credentialList = new ArrayList<>();

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // Toast.makeText(context, "constructor called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_1);
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
            db.execSQL(DROP_TABLE_1);
            onCreate(db);
        }
        catch (SQLException e) {
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean insertUser(String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        contentValues.put(KEY_ID, uuid.toString());
        contentValues.put(KEY_USERNAME, userName);
        contentValues.put(KEY_PASSWORD, password);
        // Daca nu e in baza de date, baga-l
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertCredentials(String serviceName, String userName, String password, String fkUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        contentValues.put(KEY_ID, uuid.toString());
        contentValues.put(SERVICE_NAME, serviceName);
        contentValues.put(KEY_USERNAME, userName);
        contentValues.put(KEY_PASSWORD, password);
        contentValues.put(FK_USER, fkUser);
        // Daca nu e in baza de date, baga-l
        long result = db.insert(TABLE_NAME_1, null, contentValues);
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


    SharedPreferences sharedPref;
    public static final String PREFERENCES_KEY = "preferences key";
    public static final String PREFERENCES_ID_KEY = "preferences id key";

    public List<Credential> getAllCredentials() {
        sharedPref = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        String username = sharedPref.getString(PREFERENCES_ID_KEY, "Nu merge nimica");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_1 + " WHERE " + FK_USER + " = '" + username + "'", null);

        while(res.moveToNext()) {
            int index1 = res.getColumnIndex(DatabaseHelper.KEY_ID);
            String rowid = res.getString(index1);
            int index2 = res.getColumnIndex(DatabaseHelper.SERVICE_NAME);
            String serviceName = res.getString(index2);
            int index3 = res.getColumnIndex(DatabaseHelper.KEY_USERNAME);
            String userName = res.getString(index3);
            int index4 = res.getColumnIndex(DatabaseHelper.KEY_PASSWORD);
            String password = res.getString(index4);
            int index5 = res.getColumnIndex(DatabaseHelper.FK_USER);
            String credentialOwner = res.getString(index5);

            Credential credential = new Credential(rowid, serviceName, userName, password, credentialOwner);

            credentialList.add(credential);
        }

        return credentialList;
    }

    public boolean isUserInDb(String username) {
        Cursor res = getAllUsers();
        if (res.getCount() != 0) {
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {
                if(username.equals(res.getString(res.getColumnIndex(KEY_USERNAME))))
                    return true;
            }
        }
        return false;
    }

    public boolean checkLoginCredentialsInDb(String username, String passwordToSHA1) {
        Cursor res = getAllUsers();
        if(res.getCount() != 0) {
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {
                if(username.equals(res.getString(res.getColumnIndex(KEY_USERNAME))) &&
                        passwordToSHA1.equals(res.getString(res.getColumnIndex(KEY_PASSWORD))))
                    return true;
            }
        }
        return false;
    }
}
