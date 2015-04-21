package com.whatsaround.whatsaround.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "whats_around_db";

    public static final int DATABASE_VERSION = 4;

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + QuestionsContract.TABLE_NAME;
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + QuestionsContract.TABLE_NAME + "( " +
                    QuestionsContract.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuestionsContract.Columns.ANSWER + " TEXT NOT NULL, " +
                    QuestionsContract.Columns.IMAGE_PATH + " TEXT NOT NULL " +
                    ")";


    //--------------
    //This piece of code avoids that an instance of DataBaseHelper be create externally, avoiding the creation
    //of many instances. An instance is created only one time and returned.
    //
    //This is a Design Pattern name Singleton. It is used when it doesn't make sense to have more than one instances of
    //the same class in an application. In this case, DataBaseHelper is the interface between the application and the
    //database and we don't need more than one of it. Therefore, we implement this Design Pattern.
    private static DataBaseHelper dataBaseHelper;

    public static DataBaseHelper getInstance(Context context) {

        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper(context);
        }

        return dataBaseHelper;

    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //-----------------


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}


