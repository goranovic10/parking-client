package com.example.parking.client.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.parking.client.sql.SqlConstants.SQL_CREATE_ENTRIES;
import static com.example.parking.client.sql.SqlConstants.SQL_DELETE_ENTRIES;
import static com.example.parking.client.sql.SqlConstants.SQL_SELECT_ALL;
import static com.example.parking.client.sql.SqlConstants.SQL_SELECT_NAME_CONDITION;

public class FavoriteDbHelper extends SQLiteOpenHelper {
  // If you change the database schema, you must increment the database version.
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "FeedReader.db";

  public FavoriteDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    db.execSQL(SQL_DELETE_ENTRIES);
    onCreate(db);
  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }

  public String selectAll() {
    String result = "";
    String query = SQL_SELECT_ALL;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);
    while (cursor.moveToNext()) {
      String result_0 = cursor.getString(0);
      result += result_0 + ",";
    }
    cursor.close();
    db.close();
    return result.trim();
  }

  public void insert(String name) {
    SQLiteDatabase db = this.getWritableDatabase();
    String query = String.format(SQL_SELECT_NAME_CONDITION, name);
    Cursor cursor = db.rawQuery(query, null);
    if (!cursor.moveToFirst()) {
      ContentValues values = new ContentValues();
      values.put("name", name);
      db.insert("favorite", null, values);
    }
    db.close();
  }

  public boolean delete(String name) {
    boolean result = false;
    String query = String.format(SQL_SELECT_NAME_CONDITION, name);
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
      db.delete("favorite", "name = ?", new String[] {name});
      cursor.close();
      result = true;
    }
    db.close();
    return result;
  }
}
