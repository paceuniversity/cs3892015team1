package com.whatsaround.whatsaround.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kyle on 04/16/2015.
 */
public class FlashcardsDBHelper extends SQLiteOpenHelper{
    private static final String LOGTAG = "Flashcards.WhatsAround";

    private static final String DB_Name = "flashcards.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_FLASHCARDS = "flashcards";
    public static final String COLUMN_ID = "flashcardId";
    public static final String COLUMN_PIC = "picture";
    public static final String COLUMN_WORD = "word";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_FLASHCARDS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PIC + " BLOB";

    public FlashcardsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
