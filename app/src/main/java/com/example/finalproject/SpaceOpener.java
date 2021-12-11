package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpaceOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "SpaceImageDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "SPACE_IMAGE";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_DATE = "DATE";
    public final static String COL_URL = "URL";
    public final static String COL_DESCRIPTION = "DESCRIPTION";
    public final static String COL_ID = "_id";

    public SpaceOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " text,"
                + COL_DATE + " text,"
                + COL_DESCRIPTION  + " text,"
                + COL_URL + " text);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }
}
