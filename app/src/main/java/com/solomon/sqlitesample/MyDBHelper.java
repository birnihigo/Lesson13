package com.solomon.sqlitesample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 12/15/2015.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "openLibBooksDB";

    public MyDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table books (imageId TEXT, title TEXT, author TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }
    public void addBook(String id, String title, String author){
        ContentValues values = new ContentValues(3);
        values.put("imageId", id);
        values.put("title", title);
        values.put("author", author);
        getWritableDatabase().insert("books", "imageId", values);
    }
    public Cursor getBooks(){
        return getReadableDatabase().rawQuery("SELECT * FROM books", null);
    }
}
