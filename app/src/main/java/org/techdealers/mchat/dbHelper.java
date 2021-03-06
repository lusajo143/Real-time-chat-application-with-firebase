package org.techdealers.mchat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper( Context context) {
        super(context, "PChat", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists user(username varchar(255) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String username) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("insert into user values ('" + username + "');");
    }

    public String getPhone() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from user;", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "null";
    }

    public void logout() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("drop table if exists user;");
        database.execSQL("create table if not exists user(username varchar(255) not null)");
    }

}
