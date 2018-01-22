package com.example.pcvincenzo.bikeracedata.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pcvincenzo on 15/01/18.
 */

public class RaceDbHelper extends SQLiteOpenHelper {



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "races.db";

    //Class constructor
    public RaceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //SQLite TABLE creation
    private static final String SQL_CREATE_RACES_TABLE =
            "CREATE TABLE " + RaceContract.RaceEntry.TABLE_NAME + " (" +
                    RaceContract.RaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RaceContract.RaceEntry.COLUMN_RACE_LOCATION + " TEXT NOT NULL, " +
                    RaceContract.RaceEntry.COLUMN_RACE_DATE + " TEXT NOT NULL, " +
                    RaceContract.RaceEntry.COLUMN_RACE_DURATION + " INTEGER DEFAULT 0, " +
                    RaceContract.RaceEntry.COLUMN_RACE_DISTANCE + " INTEGER NOT NULL, " +
                    RaceContract.RaceEntry.COLUMN_RACE_ELEVATION + " INTEGER NOT NULL)";

    //SQLite TABLE deleting
    private static final String SQL_DELETE_RACES_TABLE =
            "DROP TABLE IF EXISTS " + RaceContract.RaceEntry.TABLE_NAME;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RACES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_RACES_TABLE);
        onCreate(db);
    }

}
